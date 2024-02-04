package top.sharehome.schedule_task_queue.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.sharehome.schedule_task_queue.server.handler.NettyServerHandler;

/**
 * 服务端
 *
 * @author AntonyCheng
 */

public class NettyServer {

    public static void main(String[] args) {
        // 创建BossGroup和WorkerGroup
        // 说明：
        // 1、创建两个线程组，分别是bossGroup和workerGroup；
        // 2、bossGroup只是处理连接请求，真正和客户端业务处理会交给workerGroup
        // 3、两个线程组最后都会无限轮询
        // 4、bossGroup和workerGroup含有的子线程（NioEventLoop）的个数默认是实际CPU核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 使用链式编程进行设置
            serverBootstrap
                    // 设置两个线程组
                    .group(bossGroup, workerGroup)
                    // 使用NioServerSocketChannel作为服务端的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置连接保持活动状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给WorkerGroup的EventLoop对应的管道设置处理器
                    .childHandler(
                            // 创建一个通道初始化对象
                            new ChannelInitializer<SocketChannel>() {
                                // 给Pipeline设置处理器
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    // 给管道加入处理器
                                    socketChannel.pipeline().addLast(new NettyServerHandler());
                                }
                            }
                    );
            System.out.println("服务器准备好了...");

            // 这里就是启动服务器，绑定一个端口，并且同步处理，生成了一个 ChannelFuture 对象
            // 关于ChannelFuture，还有一个异步机制，这里涉及到Netty的异步模型
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            System.out.println("服务器已经启动...");

            // 对通道关闭事件进行同步监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 最后优雅的关闭两个线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
