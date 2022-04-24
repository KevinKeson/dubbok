package org.kevink.dubbok.demo.nio;

import org.kevink.dubbok.common.exception.RpcException;
import org.kevink.dubbok.demo.service.impl.ServiceImpl;
import org.kevink.dubbok.registry.ServiceRegistry;
import org.kevink.dubbok.registry.impl.DefaultServiceRegistry;
import org.kevink.dubbok.remoting.RpcServer;
import org.kevink.dubbok.remoting.nio.NioServer;

public class DemoServer {

    public static void main(String[] args) throws RpcException {
        // 注册服务
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(new ServiceImpl());
        // 启动服务
        RpcServer server = new NioServer(registry);
        server.start(6666);
    }

}
