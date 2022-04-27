package org.kevink.dubbok.serialize.jdk;

import org.kevink.dubbok.serialize.api.ObjectOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * JDK对象输出
 */
public class JdkObjectOutput implements ObjectOutput {

    private final ObjectOutputStream output;

    public JdkObjectOutput(OutputStream out) throws IOException {
        output = new ObjectOutputStream(out);
    }

    @Override
    public void writeBool(boolean val) throws IOException {
        output.writeBoolean(val);
    }

    @Override
    public void writeByte(byte val) throws IOException {
        output.writeByte(val);
    }

    @Override
    public void writeShort(short val) throws IOException {
        output.writeShort(val);
    }

    @Override
    public void writeInt(int val) throws IOException {
        output.writeInt(val);
    }

    @Override
    public void writeLong(long val) throws IOException {
        output.writeLong(val);
    }

    @Override
    public void writeFloat(float val) throws IOException {
        output.writeFloat(val);
    }

    @Override
    public void writeDouble(double val) throws IOException {
        output.writeDouble(val);
    }

    @Override
    public void writeUTF(String str) throws IOException {
        if (str == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(str.length());
            output.writeUTF(str);
        }
    }

    @Override
    public void writeBytes(byte[] buf) throws IOException {
        if (buf == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(buf.length);
            output.write(buf);
        }
    }

    @Override
    public void writeBytes(byte[] buf, int off, int len)
            throws IOException {
        if (buf == null) {
            output.writeInt(-1);
        } else {
            output.writeInt(len);
            output.write(buf, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        output.flush();
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            output.writeByte(0);
        } else {
            output.writeByte(1);
            output.writeObject(obj);
        }
    }

}
