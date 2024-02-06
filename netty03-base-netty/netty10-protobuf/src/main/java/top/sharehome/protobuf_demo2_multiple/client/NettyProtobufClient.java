package top.sharehome.protobuf_demo2_multiple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import top.sharehome.protobuf_demo2_multiple.client.handler.ProtobufClientHandler;

import java.net.InetSocketAddress;

/**
 * 多数据类型客户端
 *
 * @author AntonyCheng
 */
public class NettyProtobufClient {
    public static void main(String[] args) {
        // 创建线程池
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 在 pipeline 中加入 ProtobufEncoder 编码器
                            pipeline.addLast("encoder", new ProtobufEncoder());
                            // 在 pipeline 中加入自定义处理器
                            pipeline.addLast("handler", new ProtobufClientHandler());
                        }
                    });
            // 同步连接服务端
            // 同步的原因：需要保证客户端已经连接上服务端才能放行
            ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接服务器成功...");
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
