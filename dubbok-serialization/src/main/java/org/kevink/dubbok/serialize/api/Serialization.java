package org.kevink.dubbok.serialize.api;

public interface Serialization {

    /**
     * 序列化
     *
     * @param object 目标对象
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     *
     * @param bytes 字节数组
     * @param clazz 对象类别
     * @return 目标对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
