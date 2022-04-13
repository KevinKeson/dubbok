package org.kevink;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RpcFramework {

    private static final ExecutorService threadPool;

    static {
        long keepAliveTime = 1;
        int corePoolSize = 10, maximumPoolSizeSize = 100;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSizeSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    public static void export(Object service, int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket;
            while ((socket = server.accept()) != null) {
                threadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T refer(Class<T> clazz, String host, int port) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz},
                (proxy, method, arguments) -> {
                    // 生成请求
                    RpcRequest rpcRequest = new RpcRequest(
                            method.getDeclaringClass().getName(),
                            method.getName(),
                            method.getParameterTypes(),
                            arguments);
                    // 远程调用
                    try (Socket socket = new Socket(host, port)) {
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        output.writeObject(rpcRequest);
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        return input.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 默认返回
                    return null;
                });
    }

}
