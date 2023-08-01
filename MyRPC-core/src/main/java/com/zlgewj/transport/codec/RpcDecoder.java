package com.zlgewj.transport.codec;

import com.zlgewj.constants.SerializerConstant;
import com.zlgewj.exception.SerializerException;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.serialize.Serializer;
import com.zlgewj.transport.dto.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author zlgewj
 * @version 1.0

 */
public class RpcDecoder extends LengthFieldBasedFrameDecoder {
    public RpcDecoder() {
        super(1024, 15, 4, 0, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws SerializerException {
        try {
            Object decode = super.decode(ctx, in);
            ByteBuf buf = (ByteBuf) decode;
            int magicNum = buf.readInt();
            byte version = buf.readByte();
            byte serializerType = buf.readByte();
            byte messageType = buf.readByte();
            int requestId = buf.readInt();
            buf.readInt();
            int length = buf.readInt();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes, 0, length);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(SerializerConstant.getSerializeType(serializerType));
            if (Message.getMessageType(messageType) != null) {
                return serializer.deserialize(bytes, Message.getMessageType(messageType));
            }else {
                return serializer.deserialize(bytes,Object.class);
            }

        }catch (Exception e) {
            e.printStackTrace();
            throw new SerializerException("解码错误");
        }
    }
}
