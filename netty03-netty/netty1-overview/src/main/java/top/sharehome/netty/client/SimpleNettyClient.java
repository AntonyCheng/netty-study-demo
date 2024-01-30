package top.sharehome.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.sharehome.netty.client.handler.NettyClientHandler;

import java.net.InetSocketAddress;

/**
 * Netty入门示例代码-客户端
 * 客户端就只需要一个线程组即可
 *
 * @author AntonyCheng
 */

public class SimpleNettyClient {
    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 创建一个线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 创建客户端启动对象
            // 客户端是Bootstrap，而不是ServerBootstrap
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap
                    // 设置线程组
                    .group(eventLoopGroup)
                    // 设置客户端通道的实现类（将来会用反射处理）
                    .channel(NioSocketChannel.class)
                    // 和服务端一样设置一个处理器
                    .handler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    // 加入自己的处理器
                                    socketChannel.pipeline().addLast(new NettyClientHandler());
                                }
                            }
                    );
            System.out.println("客户端准备好了...");

            // 让客户端去连接服务端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
            // 对通道关闭事件进行同步监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 优雅关闭
            eventLoopGroup.shutdownGracefully();
        }

    }

}
