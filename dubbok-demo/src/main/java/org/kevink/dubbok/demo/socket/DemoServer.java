package org.kevink.dubbok.demo.socket;

import org.kevink.dubbok.common.exception.RpcException;
import org.kevink.dubbok.demo.service.impl.ServiceImpl;
import org.kevink.dubbok.registry.Registry;
import org.kevink.dubbok.registry.impl.DefaultRegistry;
import org.kevink.dubbok.remoting.RpcServer;
import org.kevink.dubbok.remoting.socket.SocketServer;

public class DemoServer {

    public static void main(String[] args) throws RpcException {
        // 注册服务
        Registry registry = new DefaultRegistry();
        registry.register(new ServiceImpl());
        // 启动服务
        RpcServer server = new SocketServer(registry);
        server.start(6666);
    }

}
