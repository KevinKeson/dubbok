package org.kevink.dubbok.serialize.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;

/**
 * The kryo utils learned from dubbo source code.
 */
public class KryoUtils {

    /**
     * 默认使用{@link ThreadLocal}版本的{@link KryoFactory}实现
     */
    private static final AbstractKryoFactory kryoFactory =
            new ThreadLocalKryoFactory();

    public static Kryo get() {
        return kryoFactory.getKryo();
    }

    public static void release(Kryo kryo) {
        kryoFactory.releaseKryo(kryo);
    }

    public static void register(Class<?> clazz) {
        kryoFactory.registerClass(clazz);
    }

    public static void setReferences(boolean references) {
        kryoFactory.setReferences(references);
    }

    public static void setRegistrationRequired(
            boolean registrationRequired) {
        kryoFactory.setRegistrationRequired(registrationRequired);
    }

}
