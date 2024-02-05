package top.sharehome.rpc.consumer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Netty客户端
 *
 * @author AntonyCheng
 */

public class NettyClient {

    /**
     * 创建线程池，线程数和机器CPU核数一致
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 创建 Netty 客户端处理器
     */
    private static NettyClientHandler clientHandler;

    /**
     * 编写方法，使用代理模式，获取代理对象
     */
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        System.out.println("正在使用代理模式获取代理对象...");
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},
                (proxy, method, args) -> {
                    System.out.println("正在执行代理模式进行方法调用...");
                    // 下面这些代码是客户端每调用一次 hello 方法，就会进入到该代码
                    if (clientHandler == null) {
                        initClient();
                    }
                    // 设置要发给服务器端的信息，“协议头+客户端调用API时传入的参数”
                    clientHandler.setParam(providerName + args[0]);
                    return executor.submit(clientHandler).get();
                }
        );
    }

    /**
     * 初始化客户端
     */
    private static void initClient() {
        System.out.println("正在初始化客户端...");
        clientHandler = new NettyClientHandler();
        // 创建线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 创建启动器
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    // 让 TCP 不延时
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(clientHandler);
                        }
                    });
            // 异步连接服务端
            ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接服务端成功...");
                    }
                }
            });
            ChannelFuture closeFuture = connectFuture.channel().closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("客户端关闭成功...");
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 注意：此时客户端
    }
}
