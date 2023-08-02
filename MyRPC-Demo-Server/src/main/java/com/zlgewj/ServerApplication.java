package com.zlgewj;

import com.zlgewj.annotation.RpcScan;
import com.zlgewj.config.RpcConfiguration;
import com.zlgewj.transport.server.RpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zlgewj
 * @version 1.0

 */
@RpcScan(packages = {"com.zlgewj"})
public class ServerApplication {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(RpcConfiguration.getLoadbalance());
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerApplication.class);
        RpcServer rpcServer = (RpcServer) context.getBean("rpcServer");
        rpcServer.start();
    }
}
