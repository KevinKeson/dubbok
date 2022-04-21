package org.kevink.dubbok.remoting.nio;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.remoting.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioClient extends RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(NioClient.class);

    public NioClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected Object send(RpcRequest request) {
        return null;
    }

}
