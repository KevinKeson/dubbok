package org.kevink.dubbok.remoting.socket;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.common.enums.RpcResponseCode;
import org.kevink.dubbok.common.exception.RpcException;
import org.kevink.dubbok.registry.ServiceRegistry;
import org.kevink.dubbok.remoting.RpcHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final ExecutorService threadPool;

    static {
        long keepAliveTime = 1;
        int corePoolSize = 3, maximumPoolSize = 10;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    private final ServiceRegistry serviceRegistry;

    public SocketServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void start(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("RPC Server Started ...");
            Socket socket;
            while ((socket = server.accept()) != null) {
                logger.info("RPC Client Connected ...");
                threadPool.execute(new WorkerThread(socket, serviceRegistry));
            }
        } catch (IOException e) {
            logger.error("IOException Occurred: ", e);
        } finally {
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
                // 获取请求
                RpcRequest request = (RpcRequest) input.readObject();
                String interfaceName = request.getInterfaceName();
                // 服务调用
                RpcResponse response;
                try {
                    Object service = registry.getService(interfaceName);
                    Object result = RpcHandler.handle(service, request);
                    response = RpcResponse.success(result);
                    log.info("Method Call Succeeded: {}.{}({})",
                            service.getClass().getCanonicalName(),
                            request.getMethodName(),
                            interfaceName);
                } catch (RpcException e) {
                    log.error("Class Not Found: ", e);
                    response = RpcResponse.fail(RpcResponseCode.CLASS_NOT_FOUND);
                } catch (NoSuchMethodException e) {
                    log.error("Method Not Found: ", e);
                    response = RpcResponse.fail(RpcResponseCode.METHOD_NOT_FOUND);
                } catch (Exception e) {
                    log.error("Method Call Failed: ", e);
                    response = RpcResponse.fail(RpcResponseCode.FAIL);
                }
                // 写回结果
                output.writeObject(response);
                output.flush();
            } catch (Exception e) {
                log.error("Exception Occurred: ", e);
            }
        }

    }

}
