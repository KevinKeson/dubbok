package org.kevink;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class WorkerThread implements Runnable {

    private final Socket socket;
    private final Object service;

    public WorkerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
            // 获取请求
            RpcRequest rpcRequest = (RpcRequest) input.readObject();
            // 找到方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 调用方法
            Object result = method.invoke(service, rpcRequest.getParameters());
            // 写回结果
            output.writeObject(result);
            output.flush();
            System.out.println("RPC Complete by " + Thread.currentThread());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
