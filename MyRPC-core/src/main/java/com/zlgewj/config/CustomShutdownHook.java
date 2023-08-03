package com.zlgewj.config;


import com.zlgewj.registry.zk.util.CuratorUtils;
import com.zlgewj.utils.IPV4Util;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * When the server  is closed, do something such as unregister all services
 *
 * @author shuang.kou
 * @since  2020年06月04日 13:11:00
 */
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(Objects.requireNonNull(IPV4Util.getLocalHostExactAddress()).getHostAddress(), RpcConfiguration.getPort());
            CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
        }));
    }
}
