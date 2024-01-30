package top.sharehome.task_queue.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
        // 假设此时遇到了两个大数据量计算任务，第一个需要5s，第二个需要10s，将这两个任务放入NioEventLoop异步队列中运行
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.execute(() -> {
            try {
                Thread.sleep(5 * 1000);
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer(LocalDateTime.now() + " ==> " + "hello,client! the 5s task is complete!", StandardCharsets.UTF_8));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        eventLoop.execute(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.channel().writeAndFlush(Unpooled.copiedBuffer(LocalDateTime.now() + " ==> " + "hello,client! the 10s task is complete!", StandardCharsets.UTF_8));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now + " ==> " + "任务已经提交到异步队列中！");
        ctx.writeAndFlush(Unpooled.copiedBuffer(LocalDateTime.now() + " ==> " + "tasks are running...", StandardCharsets.UTF_8));
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
