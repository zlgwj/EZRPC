package com.zlgewj.transport.client.handler;

import com.zlgewj.exception.RpcException;
import com.zlgewj.factory.SingletonFactory;
import com.zlgewj.transport.client.ResponseContainer;
import com.zlgewj.transport.client.RpcClient;
import com.zlgewj.transport.dto.ErrorMessage;
import com.zlgewj.transport.dto.Message;
import com.zlgewj.transport.dto.Pong;
import com.zlgewj.transport.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:14
 */

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ResponseContainer responseContainer;
    private final RpcClient client;

    public ClientHandler() {
        responseContainer = SingletonFactory.getInstance(ResponseContainer.class);
        client = SingletonFactory.getInstance(RpcClient.class);
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
            }else if(messageType.equals(Pong.class)) {
                log.info("pong....");
            }else if (type == 0 ) {
                ErrorMessage errorMessage = (ErrorMessage) msg;
                throw new RpcException(errorMessage.getMessage());
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("inactive.......");
        System.out.println(ctx.channel().id());
//        client.reConnect(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("client exception.......");
//        ctx.channel().close();
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);

//        throw new RpcException(cause.getMessage());
    }
}
