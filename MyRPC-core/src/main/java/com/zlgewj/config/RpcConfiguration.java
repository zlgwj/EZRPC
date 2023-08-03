package com.zlgewj.config;

import com.zlgewj.utils.PropertiesUtil;

import java.lang.reflect.Field;

/**
 * 配置集合
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */



public class RpcConfiguration {
    private static String serialize;
    private static String discovery;
    private static String registrarAddress;
    private static String loadbalance;
    private static Integer port = 1017;

    static {
        Field[] declaredFields = RpcConfiguration.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                String property = PropertiesUtil.getProperty(declaredField.getName());
                if (!"".equals(property)) {
                    if ("port".equals(declaredField.getName())) {
                        declaredField.set(null, Integer.parseInt(property));
                    }else {
                        declaredField.set(null, property);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static String getSerialize() {
        return serialize;
    }

    public static String getRegistrar() {
        return discovery;
    }

    public static String getRegistrarAddress() {
        return registrarAddress;
    }

    public static String getLoadbalance() {
        return loadbalance;
    }
    public static Integer getPort() {
        return port;
    }


}
