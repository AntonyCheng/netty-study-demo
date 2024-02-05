package top.sharehome.rpc.provider.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty服务端
 *
 * @author AntonyCheng
 */

public class NettyServer {

    /**
     * 对外暴露启动服务器方法
     */
    public static void startServer(String hostname, int port) {
        startServer0(hostname, port);
    }

    /**
     * 编写一个方法，完成对NettyServer的初始化和启动
     */
    private static void startServer0(String hostname, int port) {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加Netty自带的字符串编解码器
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            // 添加自己的业务处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 异步监听服务器启动事件
            ChannelFuture bindFuture = serverBootstrap.bind(hostname, port);
            bindFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器启动成功...");
                    }
                }
            });
            // 同步监听服务器关闭事件
            ChannelFuture closeFuture = bindFuture.channel().closeFuture().sync();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器关闭成功...");
                    }
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
