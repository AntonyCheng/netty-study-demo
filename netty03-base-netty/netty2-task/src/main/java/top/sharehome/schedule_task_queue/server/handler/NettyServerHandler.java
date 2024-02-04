package top.sharehome.schedule_task_queue.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 为服务端自定义一个Handler需要继承Netty规定好的某个handlerAdapter
 *
 * @author AntonyCheng
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 监听通道读取事件（这里可以读取客户端发送来的消息）
     * ChannelHandlerContext ctx：上下文对象，含有管道Pipeline，通道Channel以及连接地址等等
     * Object msg：客户端发送的数据，默认Object类型
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        EventLoop eventLoop = ctx.channel().eventLoop();
        ctx.writeAndFlush(Unpooled.copiedBuffer(LocalDateTime.now() + " ==> " + "the random number will appear after five seconds:", StandardCharsets.UTF_8));
        eventLoop.schedule(() -> {
            LocalDateTime now = LocalDateTime.now();
            ctx.writeAndFlush(Unpooled.copiedBuffer(now + " ==> " + "hello,client! the random number is " + new Random().nextInt(1000), StandardCharsets.UTF_8));
        }, 5L, TimeUnit.SECONDS);
    }

    /**
     * 监听异常事件
     * 处理异常一般需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("出现异常，通道关闭...");
        ctx.channel().close();
    }
}
