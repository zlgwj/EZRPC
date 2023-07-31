package com.zlgewj.transport.client;

import com.zlgewj.constants.PropertyConstant;
import com.zlgewj.exception.RpcException;
import com.zlgewj.extension.ExtensionLoader;
import com.zlgewj.factory.SingletonFactory;
import com.zlgewj.registry.ServiceDiscovery;
import com.zlgewj.transport.client.handler.ClientHandler;
import com.zlgewj.transport.codec.RpcDecoder;
import com.zlgewj.transport.codec.RpcEncoder;
import com.zlgewj.transport.dto.RpcRequest;
import com.zlgewj.transport.dto.RpcResponse;
import com.zlgewj.utils.PropertiesUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.CompleteFuture;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import jdk.nashorn.internal.runtime.Context;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;

import javax.xml.ws.Response;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:09
 */
@Slf4j
public class RpcClient {

    private final ServiceDiscovery discovery;
    private final ResponseContainer responseContainer;
    private final ChannelProvider channelProvider;
    private final Bootstrap bootstrap;

    public RpcClient() {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler());
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });
        String discoveryName = PropertiesUtil.getProperty(PropertyConstant.DISCOVERY_TYPE);
        discovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension(discoveryName);
        responseContainer = SingletonFactory.getInstance(ResponseContainer.class);
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completeFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess() ) {
                log.info("连接成功[{}]",inetSocketAddress);
                completeFuture.complete(future.channel());
            }else {
                throw new IllegalArgumentException();
            }
        });
        return completeFuture.get();
    }

    public Object sendRequest(RpcRequest request) throws RpcException, ExecutionException, InterruptedException {
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
                }
            });
        } else {
            throw new IllegalArgumentException();
        }
        return promise;
    }

    public Channel getChannel(InetSocketAddress address) throws ExecutionException, InterruptedException {

        Channel channel = channelProvider.get(address);
        if (channel == null ) {
            channel = doConnect(address);
            channelProvider.set(address,channel);
        }
        return channel;

    }
}
