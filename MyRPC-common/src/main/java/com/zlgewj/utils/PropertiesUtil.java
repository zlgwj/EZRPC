package com.zlgewj.utils;


import com.zlgewj.constants.RpcConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesUtil {
    static Properties properties;
    static {
        try (InputStream in = PropertiesUtil.class.getResourceAsStream("/rpc.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static int getServerPort() {
        String value = properties.getProperty("rpc.port");
        if(value == null) {
            return RpcConstant.DEFAULT_PORT;
        } else {
            return Integer.parseInt(value);
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}