package com.zlgewj.transport.server.handler;

import com.zlgewj.config.RpcConfiguration;
import com.zlgewj.constants.RpcMessageCode;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.provider.ServiceProvider;
import com.zlgewj.transport.dto.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author zlgewj
 * @version 1.0

 */
@ChannelHandler.Sharable
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final ServiceProvider serviceProvider;

    public ServerHandler() {
//        获取注册中心类型
        serviceProvider = ExtensionLoader.getExtensionLoader(ServiceProvider.class).getExtension(RpcConfiguration.getRegistrar());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("server exception");
        ctx.channel().close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel inactive....");
        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            int type = message.getType();
            Class<? extends Message> messageType = Message.getMessageType(type);
            if (messageType.equals(RpcRequest.class)) {
                RpcRequest request = (RpcRequest) message;
                Object service = serviceProvider.getService(request.getRpcServiceName());

                try {
                    Object invoke = invoke(service, request);
                    RpcResponse response = RpcResponse.builder()
                            .data(invoke)
                            .code(RpcMessageCode.SUCCESS)
                            .requestId(request.getRequestId())
                            .message("调用成功")
                            .build();
                    log.info("resp - {}", response);
                    ctx.writeAndFlush(response);
                } catch (Exception e) {
                    ErrorMessage errorMessage = new ErrorMessage(e.getCause().getMessage());
                    errorMessage.setRequestId(request.getRequestId());
                    ctx.writeAndFlush(errorMessage);
                }

            } else if (messageType.equals(Ping.class)) {
                log.info("pong....");
                ctx.writeAndFlush(new Pong());
            }
        }
        super.channelRead(ctx, msg);
    }

    private Object invoke(Object obj, RpcRequest request) throws Exception {
        Class<?> aClass = obj.getClass();
        Method method = aClass.getMethod(request.getMethodName(), request.getParamTypes());
        return method.invoke(obj, request.getParameters());

    }
}
