package com.zlgewj.transport.server;

import com.zlgewj.transport.codec.RpcDecoder;
import com.zlgewj.transport.codec.RpcEncoder;
import com.zlgewj.transport.server.handler.ServerHandler;
import com.zlgewj.utils.IPV4Util;
import com.zlgewj.utils.PropertiesUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.data.Id;
import org.springframework.stereotype.Component;
import sun.security.acl.WorldGroupImpl;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

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
                        pipeline.addLast(new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(serverHandler);
                        pipeline.addLast(new ChannelDuplexHandler(){
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                IdleStateEvent event = (IdleStateEvent) evt;
                                if (event.state() == IdleState.READER_IDLE) {
                                    log.info("5秒未收到你的消息");
//                                    ctx.channel().close();
                                }
                                super.userEventTriggered(ctx, evt);
                            }
                        });
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
