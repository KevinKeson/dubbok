package org.kevink.dubbok.serialize.kryo;

import com.esotericsoftware.kryo.io.Input;
import org.kevink.dubbok.serialize.api.Cleanable;
import org.kevink.dubbok.serialize.api.ObjectInput;

import java.io.IOException;
import java.io.InputStream;

/**
 * Kryo对象输入
 */
public class KryoObjectInput implements ObjectInput, Cleanable {

    private final Input input;

    public KryoObjectInput(InputStream in) {
        input = new Input(in);
    }

    @Override
    public boolean readBool() throws IOException {
        return false;
    }

    @Override
    public byte readByte() throws IOException {
        return 0;
    }

    @Override
    public short readShort() throws IOException {
        return 0;
    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public String readUTF() throws IOException {
        return null;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public Object readObject() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public <T> T readObject(Class<T> type) throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public void clean() {

    }

}
