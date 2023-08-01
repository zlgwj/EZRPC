package com.zlgewj.proxy;

import com.zlgewj.config.RpcServiceDefinition;
import com.zlgewj.transport.client.RpcClient;
import com.zlgewj.transport.dto.RpcRequest;
import com.zlgewj.transport.dto.RpcResponse;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;

/**
 * @author zlgewj
 * @version 1.0

 */

@Slf4j
public class RpcProxy implements MethodInterceptor {

    private final RpcClient client;
    private final RpcServiceDefinition serviceDefinition;

    public RpcProxy(RpcClient client, RpcServiceDefinition serviceDefinition) {
        this.client = client;
        this.serviceDefinition = serviceDefinition;
    }

    public <T> T getProxy(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setClassLoader(clazz.getClassLoader());
        enhancer.setCallback(this);
        return clazz.cast(enhancer.create());
    }



    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        log.info("调用了方法:[{}]",method.getName());
        Random random = new Random();
        random.setSeed(new Date().getTime());
        RpcRequest build = RpcRequest.builder()
                .methodName(method.getName())
                .interfaceName(serviceDefinition.getInterfaceName())
                .paramTypes(method.getParameterTypes())
                .requestId(random.nextInt())
                .group(serviceDefinition.getGroup())
                .version(serviceDefinition.getVersion())
                .parameters(objects)
                .build();
        try {
            Promise<RpcResponse> promise;
            promise = (Promise<RpcResponse>) client.sendRequest(build);
            promise.sync();
            return promise.getNow().getData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
