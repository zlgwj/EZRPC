package com.zlgewj.provider.nacos;

import com.zlgewj.config.RpcServiceDefinition;
import com.zlgewj.constants.PropertyConstant;
import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.provider.ServiceProvider;
import com.zlgewj.registry.ServiceRegistry;
import com.zlgewj.utils.IPV4Util;
import com.zlgewj.utils.PropertiesUtil;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * nacos 服务提供者
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */

public class NacosServiceProvider implements ServiceProvider {
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public NacosServiceProvider() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension(PropertiesUtil.getProperty(PropertyConstant.DISCOVERY_TYPE));
    }

    @Override
    public void addService(RpcServiceDefinition rpcServiceDefinition) {
        serviceMap.put(rpcServiceDefinition.getServiceName(),rpcServiceDefinition.getService());
        registeredService.add(rpcServiceDefinition.getServiceName());
    }

    @Override
    public Object getService(String name) throws RpcException {
        if (registeredService.contains(name)) {
            return serviceMap.get(name);
        }else {
            throw new RpcException("服务未找到");
        }
    }

    @Override
    public void publishService(RpcServiceDefinition rpcServiceDefinition) {
        addService(rpcServiceDefinition);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(IPV4Util.getLocalHostExactAddress().getHostAddress() , PropertiesUtil.getServerPort());
        serviceRegistry.registerService(rpcServiceDefinition.getServiceName(), inetSocketAddress);
    }
}
