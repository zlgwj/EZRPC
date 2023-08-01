package com.zlgewj.registry;



import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.SPI;
import com.zlgewj.transport.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author shuang.kou
 * @since  2020年06月01日 15:16:00
 */
@SPI
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest) throws RpcException;
}
