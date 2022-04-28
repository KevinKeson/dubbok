package org.kevink.dubbok.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.kevink.dubbok.serialize.api.Cleanable;
import org.kevink.dubbok.serialize.api.ObjectInput;
import org.kevink.dubbok.serialize.kryo.utils.KryoUtils;

import java.io.InputStream;

/**
 * Kryo对象输入
 */
public class KryoObjectInput implements ObjectInput, Cleanable {

    private final Input input;

    private Kryo kryo;

    public KryoObjectInput(InputStream in) {
        input = new Input(in);
        kryo = KryoUtils.get();
    }

    @Override
    public boolean readBool() {
        return input.readBoolean();
    }

    @Override
    public byte readByte() {
        return input.readByte();
    }

    @Override
    public short readShort() {
        return input.readShort();
    }

    @Override
    public int readInt() {
        return input.readInt();
    }

    @Override
    public long readLong() {
        return input.readLong();
    }

    @Override
    public float readFloat() {
        return input.readFloat();
    }

    @Override
    public double readDouble() {
        return input.readDouble();
    }

    @Override
    public String readUTF() {
        return input.readString();
    }

    @Override
    public byte[] readBytes() {
        int len = input.readInt();
        if (len < 0) {
            return null;
        } else if (len == 0) {
            return new byte[]{};
        } else {
            return input.readBytes(len);
        }
    }

    @Override
    public Object readObject() {
        return kryo.readClassAndObject(input);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> type) {
        return (T) readObject();
    }

    @Override
    public void clean() {
        KryoUtils.release(kryo);
        kryo = null;
    }

}
