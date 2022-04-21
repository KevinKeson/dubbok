package org.kevink.dubbok.demo;

import org.kevink.dubbok.common.exception.RpcException;
import org.kevink.dubbok.demo.impl.ServiceImpl;
import org.kevink.dubbok.registry.ServiceRegistry;
import org.kevink.dubbok.registry.impl.DefaultServiceRegistry;
import org.kevink.dubbok.remoting.socket.SocketServer;

public class DemoServer {

    public static void main(String[] args) throws RpcException {
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(new ServiceImpl());
        SocketServer server = new SocketServer(registry);
        server.start(6666);
    }

}
