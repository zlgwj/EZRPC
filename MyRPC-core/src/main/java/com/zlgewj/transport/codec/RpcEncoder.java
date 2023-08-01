package com.zlgewj.transport.codec;

import com.zlgewj.constants.PropertyConstant;
import com.zlgewj.enums.SerializeTypeEnum;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.serialize.Serializer;
import com.zlgewj.transport.dto.Message;
import com.zlgewj.utils.PropertiesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zlgewj
 * @version 1.0

 */

@Slf4j
public class RpcEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            int type = message.getType();
            String serializeType = PropertiesUtil.getProperty(PropertyConstant.SERIALIZER_TYPE);
            SerializeTypeEnum serializeTypeEnum = SerializeTypeEnum.getByName(serializeType);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serializeType);
            byte[] bytes = serializer.serialize(msg);
            int length = bytes.length;
//            魔数
            out.writeBytes(new byte[]{0,8,0,6});
//            版本
            out.writeByte(1);
//            序列化方式
            out.writeByte(serializeTypeEnum.getCode());
//            消息类型
            out.writeByte(message.getType());
//            request id
            out.writeInt(message.getRequestId());
//            对齐位
            out.writeInt(0xff);
//            消息长度
            log.info("消息长度 - {}",length);
            out.writeInt(length);
            out.writeBytes(bytes);
        }

    }
}
