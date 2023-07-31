package com.zlgewj;

import com.zlgewj.annotation.RpcScan;
import com.zlgewj.transport.server.RpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 16:00
 */
@RpcScan(packages = {"com.zlgewj"})
public class ServerApplication {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerApplication.class);
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");
        rpcServer.start();
    }
}
