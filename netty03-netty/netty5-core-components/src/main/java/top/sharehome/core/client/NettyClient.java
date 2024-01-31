package top.sharehome.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.sharehome.core.client.handler.ClientHandler;

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
        // 创建一个线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            // 创建启动器
            Bootstrap bootstrap = new Bootstrap();
            // 编写启动配置
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            // 连接服务端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999));
            // 添加监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接服务端成功...");
                    }else {
                        System.out.println(channelFuture.cause().getMessage());
                    }
                }
            });
            // 监听连接关闭事件
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }finally {
            // 优雅关闭线程组
            eventLoopGroup.shutdownGracefully();
        }
    }

}
