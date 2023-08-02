package com.zlgewj.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.zlgewj.constants.PropertyConstant;
import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.locdbalance.LoadBalance;
import com.zlgewj.registry.ServiceDiscovery;
import com.zlgewj.registry.nacos.util.NacosUtil;
import com.zlgewj.transport.dto.RpcRequest;
import com.zlgewj.utils.PropertiesUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * nacos 服务发现
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */

public class NacosServiceDiscovery implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public NacosServiceDiscovery() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(PropertiesUtil.getProperty(PropertyConstant.LOAD_BALANCE));
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) throws RpcException {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        try {
            List<String> services = NacosUtil.getServices(rpcServiceName);
            if (services.isEmpty()) throw new RpcException("无服务!!");
            String s = loadBalance.doSelect(services);
            String[] split = s.split(":");
            return new InetSocketAddress(split[0], Integer.parseInt(split[1]));
        } catch (NacosException e) {
            throw new RpcException(e.getMessage());
        }
    }
}
