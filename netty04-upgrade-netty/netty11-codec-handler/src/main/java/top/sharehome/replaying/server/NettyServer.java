package top.sharehome.replaying.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.sharehome.replaying.server.decoder.ServerByteToLongDecoder;
import top.sharehome.replaying.server.encoder.ServerLongToByteEncoder;
import top.sharehome.replaying.server.handler.ServerHandler;

/**
 * 服务端
 *
 * @author AntonyCheng
 */
public class NettyServer {
    public static void main(String[] args) {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入自定义出战编码器进行编码
                            pipeline.addLast(new ServerLongToByteEncoder());
                            // 加入自定义入站解码器进行解码
                            pipeline.addLast(new ServerByteToLongDecoder());
                            // 加入自定义处理器
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            // 异步监听服务器启动事件
            // 异步监听启动事件是为了让服务端在后台启动，加快速度，但是也可以同步启动
            ChannelFuture bindFuture = serverBootstrap.bind(9999);
            // 监听bindFuture绑定事件结果
            bindFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器启动成功...");
                    } else {
                        System.err.println(channelFuture.cause().getMessage());
                    }
                }
            });
            // 同步监听关闭事件
            // 同步监听关闭事件是为了让服务端关闭前就阻塞在此，不会执行finally代码块中的关闭线程组操作
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
