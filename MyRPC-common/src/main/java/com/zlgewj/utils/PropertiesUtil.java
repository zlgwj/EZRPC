package com.zlgewj.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesUtil {
    static Properties properties;
    static {
        try (InputStream in = PropertiesUtil.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if(value == null) {
            return 8080;
        } else {
            return Integer.parseInt(value);
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}