package com.zlgewj.spring;

import com.zlgewj.annotation.RpcReference;
import com.zlgewj.annotation.RpcService;
import com.zlgewj.config.RpcConfiguration;
import com.zlgewj.config.RpcServiceDefinition;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.factory.SingletonFactory;
import com.zlgewj.provider.ServiceProvider;
import com.zlgewj.proxy.RpcProxy;
import com.zlgewj.transport.client.RpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

/**
 * @author zlgewj
 * @version 1.0

 */
@Component
@Slf4j
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;
    private final RpcClient client;

    public SpringBeanPostProcessor() {
        serviceProvider = ExtensionLoader.getExtensionLoader(ServiceProvider.class).getExtension(RpcConfiguration.getRegistrar());
        client = SingletonFactory.getInstance(RpcClient.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean,@Nonnull String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceDefinition build = RpcServiceDefinition.builder()
                    .group(rpcService.group())
                    .interfaceName(rpcService.name())
                    .version(rpcService.version())
                    .service(bean)
                    .build();
            serviceProvider.publishService(build);
            log.info("service 【{}】注册成功", build.getInterfaceName());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean,@Nonnull String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference annotation = declaredField.getAnnotation(RpcReference.class);
            if (null != annotation) {
                RpcServiceDefinition build = RpcServiceDefinition.builder()
                        .group(annotation.group())
                        .version(annotation.version())
                        .interfaceName(annotation.serviceName())
                        .build();
                RpcProxy rpcProxy = new RpcProxy(client, build);
                Object rpcClient = rpcProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, rpcClient);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
