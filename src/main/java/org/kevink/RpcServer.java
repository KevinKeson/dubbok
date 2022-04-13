package org.kevink;

public class RpcServer {

    public static void main(String[] args) {
        System.out.println("RPC Server Started ...");
        RpcFramework.export(new ServiceImpl(), 6666);
    }

}
