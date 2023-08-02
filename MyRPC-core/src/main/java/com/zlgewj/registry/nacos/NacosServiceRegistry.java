package com.zlgewj.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.zlgewj.registry.ServiceRegistry;
import com.zlgewj.registry.nacos.util.NacosUtil;

import java.net.InetSocketAddress;

/**
 * nacos 的服务注册中心
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */

public class NacosServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.register(rpcServiceName, inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
