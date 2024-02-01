package top.sharehome.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.sharehome.core.server.handler.ServerHandler;

/**
 * 服务端
 *
 * @author AntonyCheng
 */
public class NettyServer {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 编写启动器配置
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 配置服务器可连接队列的大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 配置workerGroup保持连接活动状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 配置workerGroup处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ServerHandler());
                        }
                    });
            // 绑定服务器IP:Port
            ChannelFuture channelFuture = serverBootstrap.bind(9999);
            // 监听Future
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器绑定端口成功...");
                    } else {
                        System.err.println(channelFuture.cause().getMessage());
                    }
                }
            });
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
