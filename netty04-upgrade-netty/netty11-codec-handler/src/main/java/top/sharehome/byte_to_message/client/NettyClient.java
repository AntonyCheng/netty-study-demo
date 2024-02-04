package top.sharehome.byte_to_message.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.sharehome.byte_to_message.client.decoder.ClientByteToLongDecoder;
import top.sharehome.byte_to_message.client.encoder.ClientLongToByteEncoder;
import top.sharehome.byte_to_message.client.handler.ClientHandler;

import java.net.InetSocketAddress;

/**
 * 客户端
 *
 * @author AntonyCheng
 */
public class NettyClient {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入自定义入站编码器进行编码
                            pipeline.addLast(new ClientLongToByteEncoder());
                            // 加入自定义出站解码器进行解码
                            pipeline.addLast(new ClientByteToLongDecoder());
                            // 加入自定义处理器
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture bindFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999));
            bindFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
