package com.zlgewj.registry.nacos.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zlgewj.config.RpcConfiguration;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * nacos 工具类
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */

public class NacosUtil {

    private static NamingService namingService;
    static {
        String property = RpcConfiguration.getRegistrarAddress();
        Properties properties = new Properties();
        properties.put("serverAddr",property);
        try {
            namingService = NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
    public static List<String> getServices(String name) throws NacosException {
        List<Instance> allInstances = namingService.getAllInstances(name);
        return allInstances.stream().map(item -> item.getIp() + ":" + item.getPort()).collect(Collectors.toList());
    }

    public static void register(String name, String ip, int port) throws NacosException {
        namingService.registerInstance(name,ip,port);
    }

    public static void clear(String name, String ip, int port) throws NacosException {
        namingService.deregisterInstance(name, ip, port);
    }

}
