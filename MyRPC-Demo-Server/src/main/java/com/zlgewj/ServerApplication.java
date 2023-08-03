package com.zlgewj;

import com.zlgewj.annotation.RpcScan;
import com.zlgewj.transport.server.RpcStarter;

/**
 * @author zlgewj
 * @version 1.0

 */
@RpcScan(packages = {"com.zlgewj"})
public class ServerApplication {
    public static void main(String[] args) throws InterruptedException {
        RpcStarter.open(ServerApplication.class);
    }
}
