package org.kevink.dubbok.serialize.jdk;

import org.kevink.dubbok.serialize.api.ObjectInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * JDK对象输入
 */
public class JdkObjectInput implements ObjectInput {

    // learned from dubbo source code
    private static final int MAX_BYTE_ARRAY_LENGTH = 8 * 1024 * 1024;

    private final ObjectInputStream input;

    public JdkObjectInput(InputStream in) throws IOException {
        input = new ObjectInputStream(in);
    }

    @Override
    public boolean readBool() throws IOException {
        return input.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return input.readByte();
    }

    @Override
    public short readShort() throws IOException {
        return input.readShort();
    }

    @Override
    public int readInt() throws IOException {
        return input.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return input.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return input.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return input.readDouble();
    }

    @Override
    public String readUTF() throws IOException {
        int len = input.readInt();
        return len < 0 ? null : input.readUTF();
    }

    @Override
    public byte[] readBytes() throws IOException {
        int len = input.readInt();
        if (len < 0) {
            return null;
        } else if (len == 0) {
            return new byte[]{};
        } else if (len > MAX_BYTE_ARRAY_LENGTH) {
            throw new IOException(
                    "Byte array length too large: " + len);
        } else {
            byte[] buf = new byte[len];
            input.readFully(buf);
            return buf;
        }
    }

    @Override
    public Object readObject() throws IOException,
            ClassNotFoundException {
        byte b = input.readByte();
        return b == 0 ? null : input.readObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> type) throws
            IOException, ClassNotFoundException {
        return (T) readObject();
    }

}
