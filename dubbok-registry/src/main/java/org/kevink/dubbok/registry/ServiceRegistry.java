package org.kevink.dubbok.registry;

import org.kevink.dubbok.common.exception.RpcException;

public interface ServiceRegistry {

    /**
     * 注册服务
     *
     * @param service
     * @throws RpcException
     */
    void register(Object service) throws RpcException;

    /**
     * 获取服务
     *
     * @param serviceName
     * @return target service
     * @throws RpcException
     */
    Object getService(String serviceName) throws RpcException;

}
