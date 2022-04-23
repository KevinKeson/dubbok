package org.kevink.dubbok.remoting;

import org.kevink.dubbok.common.dto.RpcRequest;
import org.kevink.dubbok.common.dto.RpcResponse;
import org.kevink.dubbok.common.enums.RpcErrorMessage;
import org.kevink.dubbok.common.enums.RpcResponseCode;
import org.kevink.dubbok.common.exception.RpcException;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class RpcClient {

    protected final String host;

    protected final int port;

    protected RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(), new Class<?>[]{clazz}, new RpcProxy(this));
    }

    protected abstract Object send(RpcRequest request);

    protected Object check(
            RpcRequest request, RpcResponse response, Logger logger) throws RpcException {
        // 检查结果
        if (!response.getCode().equals(RpcResponseCode.SUCCESS.getCode())) {
            String cause = request.getInterfaceName() + "." +
                    request.getMethodName() + ", " +
                    response.getCode() + "(Code), " +
                    response.getMessage() + "(Message)";
            logger.error("Service Invocation Failed: {}", cause);
            throw new RpcException(RpcErrorMessage.SERVICE_INVOCATION_FAIL, cause);
        }
        return response.getData();
    }

    private static class RpcProxy implements InvocationHandler {

        private final RpcClient client;

        RpcProxy(RpcClient client) {
            this.client = client;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            // 生成请求
            RpcRequest request = RpcRequest.builder()
                    .interfaceName(method.getDeclaringClass().getCanonicalName())
                    .paramTypes(method.getParameterTypes())
                    .methodName(method.getName())
                    .parameters(args)
                    .build();
            // 远程调用
            return client.send(request);
        }

    }

}
