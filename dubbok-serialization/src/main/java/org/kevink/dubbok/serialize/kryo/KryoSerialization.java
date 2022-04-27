package org.kevink.dubbok.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.serialize.api.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.function.Supplier;

public class KryoSerialization implements Serialization {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerialization.class);

    /**
     * <p>1. 由于{@link Kryo}并非线程安全 每个线程都应该有单独的实例
     * 所以使用{@link ThreadLocal}用于线程隔离
     *
     * <p>2. 本质上是提供{@link Supplier#get()}模板
     * 用于搭配{@link ThreadLocal#get()}和{@link ThreadLocal#remove()}使用
     *
     * <p>3. 实际上无需每次都重新初始化新的对象 可以采用匿名内部类的方式实现线程隔离
     */
    private final ThreadLocal<Kryo> kryoThreadLocal =
            ThreadLocal.withInitial(() -> {
                Kryo kryo = new Kryo();
                kryo.register(RpcRequest.class);
                kryo.register(RpcResponse.class);
                kryo.setReferences(true);
                kryo.setRegistrationRequired(false);
                return kryo;
            });

    @Override
    public byte[] serialize(Object object) {
        try (Output output = new Output(
                new ByteArrayOutputStream())) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            logger.info("Serialization Succeeded !");
            return output.toBytes();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (Input input = new Input(
                new ByteArrayInputStream(bytes))) {
            Kryo kryo = kryoThreadLocal.get();
            T object = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            logger.info("Deserialization Succeeded !");
            return object;
        }
    }
}
