package top.sharehome.core.common.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import top.sharehome.core.common.client.handler.ClientHandler;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Scanner;

/**
 * 创建普通服务端标准示例代码
 *
 * @author AntonyCheng
 */
public class NettyClient {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 创建一个线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            Bootstrap bootstrap = new Bootstrap();
            // 编写启动器配置
            bootstrap.group(eventLoopGroup)
                    // 设置客户端通道类型
                    .channel(NioSocketChannel.class)
                    // 配置处理器
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            // 添加Netty自带的编码器
                            pipeline.addLast(new StringEncoder());
                            // 添加Netty自带的解码器
                            pipeline.addLast(new StringDecoder());
                            // 添加自定义处理器
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            // 异步连接服务端
            ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999));
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
            // 这里为了模拟向服务器发送消息，使用Scanner进行控制台获取消息，当发送“EXIT”就表示发送结束
            String content = "";
            Channel channel = connectFuture.channel();
            while (!Objects.equals(content, "EXIT")) {
                Scanner scanner = new Scanner(System.in);
                content = scanner.nextLine();
                if (content.isBlank() || Objects.equals(content, "EXIT")) {
                    continue;
                }
                channel.writeAndFlush(content);
            }
            // 关闭客户端通道
            channel.close();
            // 同步监听关闭事件
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
            // 优雅关闭线程组
            eventLoopGroup.shutdownGracefully();
        }
    }

}
