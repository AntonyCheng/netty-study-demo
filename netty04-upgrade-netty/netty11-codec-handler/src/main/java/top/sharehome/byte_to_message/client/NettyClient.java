package top.sharehome.byte_to_message.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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
            // 同步连接服务端
            // 同步的原因：需要保证客户端已经连接上服务端才能放行
            ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
            // 监听connectFuture连接事件结果
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接服务端成功...");
                    } else {
                        System.out.println(channelFuture.cause().getMessage());
                    }
                }
            });
            // 同步监听关闭事件
            // 同步监听关闭事件是为了让服务端关闭前就阻塞在此，不会执行finally代码块中的关闭线程组操作
            ChannelFuture closeFuture = connectFuture.channel().closeFuture().sync();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("断开连接服务端成功...");
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
