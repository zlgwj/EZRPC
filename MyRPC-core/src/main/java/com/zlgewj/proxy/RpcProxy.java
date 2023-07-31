package com.zlgewj.proxy;

import cn.hutool.core.util.RandomUtil;
import com.zlgewj.config.RpcServiceDefinition;
import com.zlgewj.transport.client.RpcClient;
import com.zlgewj.transport.dto.RpcRequest;
import com.zlgewj.transport.dto.RpcResponse;
import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 12:36
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
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
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
        RpcResponse<Object> response = null;
        Promise<RpcResponse<Object>> promise = (Promise<RpcResponse<Object>>) client.sendRequest(build);
        promise.await();
        return promise.get().getData();
    }
}
