package top.sharehome.unpooled;

import com.sun.net.httpserver.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import top.sharehome.unpooled.handler.NettyHttpHandler;

/**
 * 用基于 Netty 实现的 Http 服务器来演示 Unpooled 类的使用方法
 *
 * @author AntonyCheng
 */
public class NettyHttpServer {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 初始化启动器
            serverBootstrap.group(bossGroup, clientGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加Http处理器
                            pipeline.addLast("HttpHandler", new HttpServerCodec());
                            // 添加自己的处理器
                            pipeline.addLast("CustomHandler", new NettyHttpHandler());
                        }
                    });
            // 绑定端口，开启服务端
            ChannelFuture channelFuture = serverBootstrap.bind(9999);
            // 打开“启动”监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器启动成功...");
                    }
                }
            });
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            clientGroup.shutdownGracefully();
        }

    }

}
