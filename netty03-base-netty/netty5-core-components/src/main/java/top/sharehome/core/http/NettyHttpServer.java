package top.sharehome.core.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import top.sharehome.core.http.handler.ServerHandler;

/**
 * 创建HTTP服务端标准示例代码
 *
 * @author AntonyCheng
 */
public class NettyHttpServer {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
// 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 编写启动器配置
            serverBootstrap.group(bossGroup, workerGroup)
                    // 设置服务端通道类型
                    .channel(NioServerSocketChannel.class)
                    // 配置服务器可连接队列的大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 配置workerGroup保持连接活动状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 配置bossGroup处理器
                    .handler(new ChannelInitializer<NioServerSocketChannel>() {
                        @Override
                        protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioServerSocketChannel.pipeline();
                            // 添加日志处理器
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    })
                    // 配置workerGroup处理器
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            // 添加Netty自带的Http处理器
                            pipeline.addLast(new HttpServerCodec());
                            // 添加自定义处理器
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
            // 优雅关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
