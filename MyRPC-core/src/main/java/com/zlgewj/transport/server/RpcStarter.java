package com.zlgewj.transport.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * rpc 启动类
 *
 * @Author zlgewj
 * @Since 2023/8/3
 */

public class RpcStarter {
    public static void open(Class<?> clazz) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(clazz);
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");
        rpcServer.start();
    }
}
