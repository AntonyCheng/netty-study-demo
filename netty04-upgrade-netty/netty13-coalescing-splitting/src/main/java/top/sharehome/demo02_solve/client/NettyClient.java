package top.sharehome.demo02_solve.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.sharehome.demo02_solve.client.decoder.ClientDecoder;
import top.sharehome.demo02_solve.client.encoder.ClientEncoder;
import top.sharehome.demo02_solve.client.handler.ClientHandler;

import java.net.InetSocketAddress;

/**
 * 客户端
 *
 * @author AntonyCheng
 */
public class NettyClient {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加自定义消息协议编码器
                            pipeline.addLast(new ClientEncoder());
                            // 添加自定义消息协议解码器
                            pipeline.addLast(new ClientDecoder());
                            // 添加自定义处理器
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999));
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接服务器成功...");
                    }
                }
            });
            ChannelFuture closeFuture = connectFuture.channel().closeFuture().sync();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("断开连接服务器成功...");
                    }
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
