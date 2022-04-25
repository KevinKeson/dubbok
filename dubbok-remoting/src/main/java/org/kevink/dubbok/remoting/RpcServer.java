package org.kevink.dubbok.remoting;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.common.enums.RpcResponseCode;
import org.kevink.dubbok.common.exception.RpcException;
import org.kevink.dubbok.registry.Registry;
import org.slf4j.Logger;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

public abstract class RpcServer {

    protected final Registry registry;

    protected RpcServer(Registry registry) {
        this.registry = registry;
    }

    protected static RpcResponse handle(
            Object input, Registry registry, Logger logger) {

        // 在各种情况下保证均有响应
        // 1. SUCCESS
        // 2. CLASS_NOT_FOUND
        // 3. METHOD_NOT_FOUND
        // 4. FAIL

        try {
            // 获取请求
            RpcRequest request = input instanceof ObjectInputStream ?
                    (RpcRequest) ((ObjectInputStream) input).readObject() :
                    (RpcRequest) input;
            String interfaceName = request.getInterfaceName();
            Object service = registry.getService(interfaceName);
            // 找到方法
            Method method = service.getClass().getMethod(
                    request.getMethodName(),
                    request.getParamTypes());
            // 调用方法
            Object result = method.invoke(service, request.getParameters());
            logger.info("Method Call Succeeded: {}.{}({})",
                    service.getClass().getCanonicalName(),
                    request.getMethodName(),
                    interfaceName);
            return RpcResponse.success(result);
        } catch (RpcException e) {
            logger.error("Class Not Found: ", e);
            return RpcResponse.fail(RpcResponseCode.CLASS_NOT_FOUND);
        } catch (NoSuchMethodException e) {
            logger.error("Method Not Found: ", e);
            return RpcResponse.fail(RpcResponseCode.METHOD_NOT_FOUND);
        } catch (Exception e) {
            logger.error("Method Call Failed: ", e);
            return RpcResponse.fail(RpcResponseCode.FAIL);
        }
    }

    public abstract void start(int port);

}
