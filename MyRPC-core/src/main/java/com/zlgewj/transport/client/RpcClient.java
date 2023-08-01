package com.zlgewj.transport.client;

import com.zlgewj.constants.PropertyConstant;
import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.factory.SingletonFactory;
import com.zlgewj.registry.ServiceDiscovery;
import com.zlgewj.transport.client.handler.ClientHandler;
import com.zlgewj.transport.codec.RpcDecoder;
import com.zlgewj.transport.codec.RpcEncoder;
import com.zlgewj.transport.dto.Ping;
import com.zlgewj.transport.dto.RpcRequest;
import com.zlgewj.transport.dto.RpcResponse;
import com.zlgewj.utils.PropertiesUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:09
 */
@Slf4j
public class RpcClient {

    private final ServiceDiscovery discovery;
    private final ResponseContainer responseContainer;
    private final ChannelPool channelProvider;
    private final Bootstrap bootstrap;
    private final NioEventLoopGroup eventLoopGroup;

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public RpcClient() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(5,3,0, TimeUnit.SECONDS));
//                        pipeline.addLast(new LoggingHandler());
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new ClientHandler());
                        pipeline.addLast(new ChannelDuplexHandler() {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                IdleStateEvent event = (IdleStateEvent) evt;
                                if (event.state() == IdleState.WRITER_IDLE) {
                                    ctx.writeAndFlush(new Ping());
                                }
                                if (event.state() == IdleState.READER_IDLE) {

                                }
                            }
                        });

                    }
                });
        String discoveryName = PropertiesUtil.getProperty(PropertyConstant.DISCOVERY_TYPE);
        discovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension(discoveryName);
        responseContainer = SingletonFactory.getInstance(ResponseContainer.class);
        channelProvider = SingletonFactory.getInstance(ChannelPool.class);
    }

    public Channel doConnect(InetSocketAddress inetSocketAddress) throws  RpcException {
        CompletableFuture<Channel> completeFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess() ) {
                log.info("连接成功[{}]",inetSocketAddress);
                completeFuture.complete(future.channel());
            }else {
                if (atomicInteger.get()<=5) {
                    log.info("连接失败，重试第{}次", atomicInteger.get());
                    atomicInteger.addAndGet(1);
                    completeFuture.complete(doConnect(inetSocketAddress));
                }else {
                    atomicInteger.set(1);
                    log.error("error:连接失败");
                    throw new RpcException("连接失败");
                }
            }
        });
        try {
            return completeFuture.get(26,TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RpcException("连接超时。。。");
        }
    }

    public Channel reConnect(Channel channel) throws RpcException {
        InetSocketAddress inetSocketAddress = channelProvider.getInetSocketAddress(channel.id().toString());
        return getChannel(inetSocketAddress);
    }

    //消息发送失败不重传，直接抛出异常
    public Object sendRequest(RpcRequest request) throws RpcException {
        InetSocketAddress inetSocketAddress = discovery.lookupService(request);
        Channel channel = getChannel(inetSocketAddress);
        DefaultPromise<RpcResponse<Object>> promise = new DefaultPromise<>(channel.eventLoop());
        if (channel.isActive()) {
            responseContainer.put(request.getRequestId(),promise);
            channel.writeAndFlush(request).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("发送成功",request);
                }else {
                    future.channel().close();
                    promise.setFailure(future.cause());
                    log.error("发送失败",future.cause());
                    throw new RpcException("远程调用失败");
                }
            });
        } else {
            log.info("channel is inactive");

        }
        return promise;
    }

    public Channel getChannel(InetSocketAddress address) throws  RpcException {

        Channel channel = channelProvider.getChannel(address);
        if (channel == null ) {
            channel = doConnect(address);
            channelProvider.set(address,channel);
        }
        return channel;

    }
    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
