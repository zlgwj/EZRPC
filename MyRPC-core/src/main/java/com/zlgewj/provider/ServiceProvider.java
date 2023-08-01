package com.zlgewj.provider;

import com.zlgewj.config.RpcServiceDefinition;
import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.SPI;

/**
 * @author zlgewj
 * @version 1.0

 */
@SPI
public interface ServiceProvider {
    void addService(RpcServiceDefinition rpcServiceDefinition);
    Object getService(String name) throws RpcException;
    void publishService(RpcServiceDefinition rpcServiceDefinition);
}
