package org.kevink.dubbok.remoting.nio;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.remoting.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient extends RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NioClient.class);

    public NioClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected Object send(RpcRequest request) {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.connect(new InetSocketAddress(host, port));
            logger.info("RPC Client Connecting ...");
            // 发起调用
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(out);
            output.writeObject(request);
            ByteBuffer buffer = ByteBuffer.wrap(out.toByteArray());
            channel.write(buffer);

            // 可能出现IO异常导致获取超时
            // 需要超时失败机制来避免死锁

            // 获取结果
            channel.read(buffer);
            ByteArrayInputStream in = new ByteArrayInputStream(buffer.array());
            ObjectInputStream input = new ObjectInputStream(in);
            RpcResponse response = (RpcResponse) input.readObject();

            // 返回结果
            return check(request, response, logger);
        } catch (Exception e) {
            logger.error("Exception Occurred: ", e);
        }
        // 默认返回
        return null;
    }

}
