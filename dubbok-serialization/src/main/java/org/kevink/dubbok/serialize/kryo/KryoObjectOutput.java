package org.kevink.dubbok.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.kevink.dubbok.serialize.api.Cleanable;
import org.kevink.dubbok.serialize.api.ObjectOutput;
import org.kevink.dubbok.serialize.kryo.utils.KryoUtils;

import java.io.OutputStream;

/**
 * Kryo对象输出
 */
public class KryoObjectOutput implements ObjectOutput, Cleanable {

    private final Output output;

    private Kryo kryo;

    public KryoObjectOutput(OutputStream out) {
        output = new Output(out);
        kryo = KryoUtils.get();
    }

    @Override
    public void writeBool(boolean val) {
        output.writeBoolean(val);
    }

    @Override
    public void writeByte(byte val) {
        output.writeByte(val);
    }

    @Override
    public void writeShort(short val) {
        output.writeShort(val);
    }

    @Override
    public void writeInt(int val) {
        output.writeInt(val);
    }

    @Override
    public void writeLong(long val) {
        output.writeLong(val);
    }

    @Override
    public void writeFloat(float val) {
        output.writeFloat(val);
    }

    @Override
    public void writeDouble(double val) {
        output.writeDouble(val);
    }

    @Override
    public void writeUTF(String str) {
        output.writeString(str);
    }

    @Override
    public void writeBytes(byte[] buf) {
        if (buf == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(buf.length);
            output.write(buf);
        }
    }

    @Override
    public void writeBytes(byte[] buf, int off, int len) {
        if (buf == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(len);
            output.write(buf, off, len);
        }
    }

    @Override
    public void flush() {
        output.flush();
    }

    @Override
    public void writeObject(Object obj) {
        kryo.writeClassAndObject(output, obj);
    }

    @Override
    public void clean() {
        KryoUtils.release(kryo);
        kryo = null;
    }

}
