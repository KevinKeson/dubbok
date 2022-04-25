package org.kevink.dubbok.demo.netty;

import org.kevink.dubbok.demo.service.Service;
import org.kevink.dubbok.remoting.RpcClient;
import org.kevink.dubbok.remoting.netty.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DemoClient {

    private static final Logger logger = LoggerFactory.getLogger(DemoClient.class);

    public static void main(String[] args) throws IOException {
        // 启动客户
        RpcClient client = new NettyClient("127.0.0.1", 6666);
        Service service = client.getProxy(Service.class);
        // 远程调用
        logger.info("RPC Client Started ...");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        String name;
        while ((name = in.readLine()) != null) {
            String result = service.hello(name);
            // 防止NPE问题
            if (result != null) {
                System.out.println(result);
            }
        }
    }

}
