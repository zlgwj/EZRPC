package com.zlgewj.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zlgewj.exception.SerializerException;
import com.zlgewj.serialize.Serializer;
import com.zlgewj.transport.dto.ErrorMessage;
import com.zlgewj.transport.dto.RpcRequest;
import com.zlgewj.transport.dto.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 18:34
 */
public class KryoSerializer implements Serializer {
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.register(ErrorMessage.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        try (
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Output output = new Output(byteArrayOutputStream)
        ) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output,obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializerException {
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                Input input = new Input(bytes)
        ) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (IOException e) {
            throw new SerializerException(e.getMessage());
        }
    }
}
