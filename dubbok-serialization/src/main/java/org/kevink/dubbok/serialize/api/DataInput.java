package org.kevink.dubbok.serialize.api;

import java.io.IOException;

/**
 * 数据输入
 */
public interface DataInput {

    /**
     * 读取bool值
     *
     * @return bool值
     * @throws IOException IO异常
     */
    boolean readBool() throws IOException;

    /**
     * 读取byte值
     *
     * @return 8位byte值
     * @throws IOException IO异常
     */
    byte readByte() throws IOException;

    /**
     * 读取short值
     *
     * @return 16位short值
     * @throws IOException IO异常
     */
    short readShort() throws IOException;

    /**
     * 读取int值
     *
     * @return 32位int值
     * @throws IOException IO异常
     */
    int readInt() throws IOException;

    /**
     * 读取long值
     *
     * @return 64位long值
     * @throws IOException IO异常
     */
    long readLong() throws IOException;

    /**
     * 读取float值
     *
     * @return 32位float值
     * @throws IOException IO异常
     */
    float readFloat() throws IOException;

    /**
     * 读取double值
     *
     * @return 64位double值
     * @throws IOException IO异常
     */
    double readDouble() throws IOException;

    /**
     * 读取UTF格式字符串
     *
     * @return UTF-8格式字符串
     * @throws IOException IO异常
     */
    String readUTF() throws IOException;

    /**
     * 读取byte数组
     *
     * @return byte数组
     * @throws IOException IO异常
     */
    byte[] readBytes() throws IOException;

}
