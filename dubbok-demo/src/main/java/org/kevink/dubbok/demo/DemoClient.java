package org.kevink.dubbok.demo;

import org.kevink.dubbok.remoting.socket.SocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DemoClient {

    private static final Logger logger = LoggerFactory.getLogger(DemoClient.class);

    public static void main(String[] args) throws IOException {
        SocketClient client = new SocketClient("127.0.0.1", 6666);
        Service service = client.getProxy(Service.class);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        logger.info("RPC Client Started ...");
        String name;
        while ((name = in.readLine()) != null) {
            System.out.println(service.hello(name));
        }
    }

}
