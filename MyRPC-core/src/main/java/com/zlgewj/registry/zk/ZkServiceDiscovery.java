package com.zlgewj.registry.zk;


import cn.hutool.core.collection.CollectionUtil;
import com.zlgewj.config.RpcConfiguration;
import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.locdbalance.LoadBalance;
import com.zlgewj.registry.ServiceDiscovery;
import com.zlgewj.registry.zk.util.CuratorUtils;
import com.zlgewj.transport.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * service discovery based on zookeeper
 *
 * @author shuang.kou
 * @since  2020年06月01日 15:16:00
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(RpcConfiguration.getLoadbalance());
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) throws RpcException {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException("服务未找到"+rpcServiceName);
        }
        // load balancing
        System.out.println(serviceUrlList);
        String targetServiceUrl = loadBalance.doSelect(serviceUrlList);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
