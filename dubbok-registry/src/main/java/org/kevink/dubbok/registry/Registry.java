package org.kevink.dubbok.registry;

import org.kevink.dubbok.common.exception.RpcException;

public interface Registry {

    /**
     * 注册服务
     *
     * @param service 目标服务
     * @throws RpcException RPC异常
     */
    void register(Object service) throws RpcException;

    /**
     * 获取服务
     *
     * @param serviceName 服务名称
     * @return 目标服务
     * @throws RpcException RPC异常
     */
    Object getService(String serviceName) throws RpcException;

}
