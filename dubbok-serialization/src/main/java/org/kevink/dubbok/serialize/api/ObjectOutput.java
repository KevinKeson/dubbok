package org.kevink.dubbok.serialize.api;

import java.io.IOException;

/**
 * 对象输出
 */
public interface ObjectOutput extends DataOutput {

    /**
     * 写出对象
     *
     * @param obj 目标对象
     * @throws IOException IO异常
     */
    void writeObject(Object obj) throws IOException;

}
