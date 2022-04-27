package org.kevink.dubbok.serialize.api;

import java.io.IOException;

/**
 * 数据输出
 */
public interface DataOutput {

    /**
     * 写出bool值
     *
     * @param val bool值
     * @throws IOException IO异常
     */
    void writeBool(boolean val) throws IOException;

    /**
     * 写出byte值
     *
     * @param val 8位byte值
     * @throws IOException IO异常
     */
    void writeByte(byte val) throws IOException;

    /**
     * 写出short值
     *
     * @param val 16位short值
     * @throws IOException IO异常
     */
    void writeShort(short val) throws IOException;

    /**
     * 写出int值
     *
     * @param val 32位int值
     * @throws IOException IO异常
     */
    void writeInt(int val) throws IOException;

    /**
     * 写出long值
     *
     * @param val 64位long值
     * @throws IOException IO异常
     */
    void writeLong(long val) throws IOException;

    /**
     * 写出float值
     *
     * @param val 32位float值
     * @throws IOException IO异常
     */
    void writeFloat(float val) throws IOException;

    /**
     * 写出double值
     *
     * @param val 64位double值
     * @throws IOException IO异常
     */
    void writeDouble(double val) throws IOException;

    /**
     * 写出UTF格式字符串
     *
     * @param str UTF-8格式字符串
     * @throws IOException IO异常
     */
    void writeUTF(String str) throws IOException;

    /**
     * 写出byte数组
     *
     * @param buf byte数组
     * @throws IOException IO异常
     */
    void writeBytes(byte[] buf) throws IOException;

    /**
     * 写出byte数组
     *
     * @param buf byte数组
     * @param off 数据偏移
     * @param len 待写长度
     * @throws IOException IO异常
     */
    void writeBytes(byte[] buf, int off, int len)
            throws IOException;

    /**
     * 推送数据流
     *
     * @throws IOException IO异常
     */
    void flush() throws IOException;

}
