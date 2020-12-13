package com.kkhill.core.scheduler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * use netty to schedule I/O tasks
 */
public class AsyncScheduler {

    private final Logger logger = LoggerFactory.getLogger(AsyncScheduler.class);

    private EventLoopGroup group = new NioEventLoopGroup();
    private ServerBootstrap bootstrap = new ServerBootstrap();

    public AsyncScheduler() throws Exception {
    }

    public void start() throws InterruptedException {
        try {
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(8080))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast();
                        }
                    });

            ChannelFuture f = bootstrap.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
