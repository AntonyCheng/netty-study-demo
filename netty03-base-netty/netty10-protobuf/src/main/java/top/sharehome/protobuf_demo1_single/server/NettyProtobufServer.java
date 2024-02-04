package top.sharehome.protobuf_demo1_single.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import top.sharehome.protobuf_demo0_init.pojo.Single;
import top.sharehome.protobuf_demo1_single.server.handler.ProtobufServerHandler;

/**
 * 单数据类型服务端
 *
 * @author AntonyCheng
 */
public class NettyProtobufServer {

    public static void main(String[] args) {
        // 创建线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<NioServerSocketChannel>() {
                        @Override
                        protected void initChannel(NioServerSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    })
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*
                            在 pipeline 中加入 ProtobufDecoder 解码器
                            参数说明：
                            1、MessageLite prototype 即指定需要解码的对象市里
                             */
                            pipeline.addLast("decoder", new ProtobufDecoder(Single.Student.getDefaultInstance()));
                            // 在 pipeline 中加入自定义处理器
                            pipeline.addLast(new ProtobufServerHandler());
                        }
                    });
            // 异步绑定端口
            ChannelFuture bindFuture = serverBootstrap.bind(9999);
            bindFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("服务器启动成功...");
                    }
                }
            });
            // 同步监听关闭服务器
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
