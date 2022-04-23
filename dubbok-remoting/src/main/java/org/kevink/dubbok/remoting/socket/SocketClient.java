package org.kevink.dubbok.remoting.socket;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.remoting.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient extends RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public SocketClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected Object send(RpcRequest request) {
        try (Socket socket = new Socket(host, port)) {
            logger.info("RPC Client Connecting ...");
            // 发起调用
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(request);

            // 可能出现IO异常导致获取超时
            // 需要超时失败机制来避免死锁

            // 获取结果
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
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
