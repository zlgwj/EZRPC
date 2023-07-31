package com.zlgewj.transport.client.handler;

import com.zlgewj.factory.SingletonFactory;
import com.zlgewj.transport.client.ResponseContainer;
import com.zlgewj.transport.dto.Message;
import com.zlgewj.transport.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:14
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ResponseContainer responseContainer;

    public ClientHandler() {
        responseContainer = SingletonFactory.getInstance(ResponseContainer.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            int type = message.getType();
            Class<? extends Message> messageType = Message.getMessageType(type);
            if (messageType.equals(RpcResponse.class)) {
                RpcResponse<Object> rpcResponse = (RpcResponse) msg;
                responseContainer.complete(rpcResponse);
            }
        }
        super.channelRead(ctx, msg);
    }
}
