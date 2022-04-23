package org.kevink.dubbok.remoting.socket;

import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.registry.ServiceRegistry;
import org.kevink.dubbok.remoting.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer extends RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final ExecutorService threadPool;

    static {
        long keepAliveTime = 1;
        int corePoolSize = 3, maximumPoolSize = 10;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MINUTES,
                workQueue,
                threadFactory);
    }

    public SocketServer(ServiceRegistry serviceRegistry) {
        super(serviceRegistry);
    }

    @Override
    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("RPC Server Started ...");
            Socket socket;
            while ((socket = server.accept()) != null) {
                logger.info("RPC Client Connected ...");
                threadPool.execute(
                        new WorkerThread(socket, serviceRegistry));
            }
        } catch (IOException e) {
            logger.error("IOException Occurred: ", e);
        } finally {
            // 出现IO异常 关闭线程池
            threadPool.shutdown();
        }
    }

    private static class WorkerThread implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(WorkerThread.class);

        private final Socket socket;

        private final ServiceRegistry registry;

        WorkerThread(Socket socket, ServiceRegistry registry) {
            this.socket = socket;
            this.registry = registry;
        }

        @Override
        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                // 服务调用
                RpcResponse response = handle(input, registry, log);

                // 可能出现IO异常导致返回失败
                // 需要机制保证客户端能够收到
                // 1. 定次重传 连续重传3次
                // 2. 定时重传 重传3次 每隔1分钟
                // 3. 混合机制 连续3次 + 重传3次 1 3 10分钟

                // 写回结果
                output.writeObject(response);
                output.flush();
            } catch (IOException e) {
                log.error("IOException Occurred: ", e);
            }
        }

    }

}
