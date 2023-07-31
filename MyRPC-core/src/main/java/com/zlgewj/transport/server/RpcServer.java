package com.zlgewj.transport.server;

import com.zlgewj.transport.codec.RpcDecoder;
import com.zlgewj.transport.codec.RpcEncoder;
import com.zlgewj.transport.server.handler.ServerHandler;
import com.zlgewj.utils.IPV4Util;
import com.zlgewj.utils.PropertiesUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.security.acl.WorldGroupImpl;

import java.net.InetSocketAddress;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 10:57
 */
@Component
@Slf4j
public class RpcServer {

    private final NioEventLoopGroup boss = new NioEventLoopGroup();
    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    public void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ServerHandler serverHandler = new ServerHandler();

        serverBootstrap = serverBootstrap.channel(NioServerSocketChannel.class)
                .group(boss, worker)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler());
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(serverHandler);
                    }
                });
        ChannelFuture sync = serverBootstrap.bind(new InetSocketAddress(IPV4Util.getLocalHostExactAddress().getHostAddress(), PropertiesUtil.getServerPort()));
        sync.addListener((ChannelFutureListener) future -> {
            System.out.println(future.cause());
            if(future.isSuccess()) {
                log.info("启动成功...");
            }
        });
        sync.channel().closeFuture().sync();
        boss.shutdownGracefully();
        worker.shutdownGracefully();

    }

}
