package com.zlgewj.registry;


import com.zlgewj.extension.SPI;

import java.net.InetSocketAddress;

/**
 * service registration
 *
 * @author shuang.kou
 * @since  2020年05月13日 08:39:00
 */
@SPI
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
