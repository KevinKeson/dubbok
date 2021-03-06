package org.kevink.dubbok.registry.impl;

import org.kevink.dubbok.common.enums.RpcErrorMessage;
import org.kevink.dubbok.common.exception.RpcException;
import org.kevink.dubbok.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRegistry implements Registry {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRegistry.class);

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public void register(Object service) throws RpcException {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);

        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException(RpcErrorMessage.SERVICE_NOT_IMPLEMENTED);
        }
        for (Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }

        logger.info("Add service:{} for interface(s):{}",
                serviceName,
                interfaces);
    }

    @Override
    public Object getService(String serviceName) throws RpcException {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcErrorMessage.SERVICE_NOT_FOUND);
        }
        logger.info("Get service:{} for interface:{}",
                service.getClass().getCanonicalName(),
                serviceName);
        return service;
    }
}
