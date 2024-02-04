package top.sharehome.replaying.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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
            ChannelFuture bindFuture = serverBootstrap.bind(9999);
            bindFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
