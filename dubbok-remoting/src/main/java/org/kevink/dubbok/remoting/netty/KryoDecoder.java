package org.kevink.dubbok.remoting.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import org.kevink.dubbok.serialize.api.Serialization;

import java.util.List;

@AllArgsConstructor
public class KryoDecoder extends ByteToMessageDecoder {

    /**
     * Netty将消息长度存储在{@link ByteBuf}头部
     */
    private static final int HEAD_LENGTH = 4;

    private final Serialization deserializer;

    private final Class<?> clazz;

    @Override
    protected void decode(
            ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) {
        // 1. 获取可读缓存字节个数
        // 值为writerIndex - readerIndex
        int length = buf.readableBytes();
        // 2. 至少应该大于头部长度
        if (HEAD_LENGTH < length) {
            // 3. 标记readerIndex位置
            buf.markReaderIndex();
            // 4. 获取对象字节数组长度
            int len = buf.readInt();
            // 5. 若为消息不完整的情况
            if (length < len) {
                // 重置readerIndex位置
                buf.resetReaderIndex();
                return;
            }
            // 6. 读取字节数组反序列化
            byte[] bytes = new byte[len];
            buf.readBytes(bytes);
            Object obj = deserializer.deserialize(bytes, clazz);
            list.add(obj);
        }
    }

}
