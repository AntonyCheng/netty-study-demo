package top.sharehome.chat.client02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import top.sharehome.chat.client02.handler.ChatClientHandler;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Scanner;

/**
 * Netty多人聊天室客户端
 *
 * @author AntonyCheng
 */
public class NettyChatClient {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 创建线程组
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
                            // 加入Netty自带的解码器
                            pipeline.addLast(new StringDecoder());
                            // 加入Netty自带的编码器
                            pipeline.addLast(new StringEncoder());
                            // 加入自定义处理器
                            pipeline.addLast(new ChatClientHandler());
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
            // 从控制台输入内容，当内容为"EXIT"时退出聊天室
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
