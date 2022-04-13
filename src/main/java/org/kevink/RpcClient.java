package org.kevink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RpcClient {

    public static void main(String[] args) throws IOException {
        System.out.println("RPC Client Started ...");
        Service service = RpcFramework.refer(Service.class, "127.0.0.1", 6666);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(service.hello(line));
        }
    }

}
