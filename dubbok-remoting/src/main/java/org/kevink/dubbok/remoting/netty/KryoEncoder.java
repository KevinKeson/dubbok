package org.kevink.dubbok.remoting.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.kevink.dubbo.serialize.Serialization;

@AllArgsConstructor
public class KryoEncoder extends MessageToByteEncoder<Object> {

    private final Serialization serializer;

    private final Class<?> clazz;

    @Override
    protected void encode(
            ChannelHandlerContext ctx, Object obj, ByteBuf buf) {
        // 1. 确保对象为实例化类型
        if (clazz.isInstance(obj)) {
            // 2. 将对象转换为字节数组
            byte[] bytes = serializer.serialize(obj);
            // 3. 获取对象字节数组长度
            int len = bytes.length;
            // 4. 设置writerIndex避免
            // IndexOutOfBounds异常
            buf.writeInt(len);
            // 5. 将字节数组写入缓冲区
            buf.writeBytes(bytes);
        }
    }

}
