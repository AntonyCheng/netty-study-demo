package top.sharehome.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

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
        System.out.println("ChannelHandlerContext：" + ctx);
        // 将msg转成ByteBuffer
        // 说明：ByteBuf是Netty提供的，而ByteBuffer是Java NIO提供的，前者性能更高
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + buf.toString(StandardCharsets.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    /**
     * 监听管道数据读取完毕事件
     * 在处理完数据后对数据进行响应
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // writeAndFlush 是 write + flush
        // flush 指的是刷新缓冲区，如果只写入了没有刷新，那么可能导致在缓冲区中真实拿到的数据得不到变化
        // 一般来说不会直接使用对象（String、实体类等）进行响应，先要对其进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client", StandardCharsets.UTF_8));
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
