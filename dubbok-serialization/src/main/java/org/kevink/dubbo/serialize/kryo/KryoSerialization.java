package org.kevink.dubbo.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.kevink.dubbo.serialize.Serialization;
import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerialization implements Serialization {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerialization.class);

    private static final ThreadLocal<Kryo> kryoThreadLocal =
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
            return object;
        }
    }
}
