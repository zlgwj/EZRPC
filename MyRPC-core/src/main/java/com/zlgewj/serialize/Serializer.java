package com.zlgewj.serialize;

import com.zlgewj.exception.SerializerException;
import com.zlgewj.extension.SPI;

/**
 * @author zlgewj
 * @version 1.0

 */
@SPI
public interface Serializer {
    /**
     * 序列化
     * @param obj 要序列化的对象
     * @return 序列化的字节数组
     */
    byte[] serialize(Object obj) throws SerializerException;

    /**
     * 反序列化
     * @param bytes 要反序列化的字节数组
     * @param clazz 反序列化的类型
     * @return  序列化结果
     * @param <T> 类型
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializerException;
}
