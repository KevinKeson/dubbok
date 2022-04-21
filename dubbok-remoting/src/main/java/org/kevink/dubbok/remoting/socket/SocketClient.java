package org.kevink.dubbok.remoting.socket;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.common.enums.RpcErrorMessage;
import org.kevink.dubbok.common.enums.RpcResponseCode;
import org.kevink.dubbok.common.exception.RpcException;
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
            logger.info("RPC Client Started ...");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(request);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            RpcResponse response = (RpcResponse) input.readObject();
            if (!response.getCode().equals(RpcResponseCode.SUCCESS.getCode())) {
                String cause = request.getInterfaceName() + "." +
                        request.getMethodName() + ", " +
                        response.getCode() + "(Code), " +
                        response.getMessage() + "(Message)";
                logger.error("Service Invocation Failed: {}", cause);
                throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAIL, cause);
            }
            return response.getData();
        } catch (Exception e) {
            logger.error("Exception Occurred: ", e);
        }
        return null;
    }

}
