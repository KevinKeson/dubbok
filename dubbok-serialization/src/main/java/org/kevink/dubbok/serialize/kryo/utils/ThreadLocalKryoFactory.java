package org.kevink.dubbok.serialize.kryo.utils;

import com.esotericsoftware.kryo.Kryo;

import java.util.function.Supplier;

public class ThreadLocalKryoFactory extends AbstractKryoFactory {

    /**
     * <p>1. 由于{@link Kryo}并非线程安全
     * 所以使用{@link ThreadLocal}用于线程隔离
     *
     * <p>2. 本质上是提供{@link Supplier#get()}模板
     * 用于{@link ThreadLocal#get()}
     */
    private final ThreadLocal<Kryo> kryoThreadLocal =
            ThreadLocal.withInitial(this::create);

    @Override
    public Kryo getKryo() {
        return kryoThreadLocal.get();
    }

    @Override
    public void releaseKryo(Kryo kryo) {
        kryoThreadLocal.remove();
    }

}
