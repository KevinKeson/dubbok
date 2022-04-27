package org.kevink.dubbok.serialize.api;

import java.io.IOException;

/**
 * 对象输入
 */
public interface ObjectInput extends DataInput {

    /**
     * 读取对象
     *
     * @return 目标对象
     * @throws IOException            IO异常
     * @throws ClassNotFoundException 已序列化对象的类未找到导致异常
     */
    Object readObject() throws IOException,
            ClassNotFoundException;

    /**
     * 读取对象
     *
     * @param type 对象类型
     * @return 目标对象
     * @throws IOException            IO异常
     * @throws ClassNotFoundException 已序列化对象的类未找到导致异常
     */
    <T> T readObject(Class<T> type) throws
            IOException, ClassNotFoundException;

}
