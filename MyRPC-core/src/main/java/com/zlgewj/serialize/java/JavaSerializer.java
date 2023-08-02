package com.zlgewj.serialize.java;

import com.zlgewj.exception.SerializerException;
import com.zlgewj.serialize.Serializer;

import java.io.*;

/**
 * java 序列化
 *
 * @Author zlgewj
 * @Since 2023/8/2
 */

public class JavaSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializerException {
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object o = objectInputStream.readObject();
            return clazz.cast(o);
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializerException(e.getMessage());
        }
    }
}
