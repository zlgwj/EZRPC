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

    static {
        Field[] declaredFields = RpcConfiguration.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                declaredField.set(null, PropertiesUtil.getProperty(declaredField.getName()));
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


}
