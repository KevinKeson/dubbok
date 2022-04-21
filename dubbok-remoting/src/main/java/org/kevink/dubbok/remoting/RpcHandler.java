package org.kevink.dubbok.remoting;

import org.kevink.dubbok.common.dto.RpcRequest;

import java.lang.reflect.Method;

public class RpcHandler {

    private RpcHandler() {
    }

    public static Object handle(Object service, RpcRequest request) throws Exception {
        // 找到方法
        Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
        // 调用方法
        return method.invoke(service, request.getParameters());
    }

}
