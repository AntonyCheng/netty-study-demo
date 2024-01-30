package top.sharehome.future.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * 为客户端自定义一个Handler需要继承Netty规定好的某个handlerAdapter
 *
 * @author AntonyCheng
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 监听通道准备就绪的事件
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ChannelHandlerContext ctx：" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server", StandardCharsets.UTF_8));
    }

    /**
     * 监听通道读取就绪的事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器响应的数据是：" + buf.toString(StandardCharsets.UTF_8));
        System.out.println("服务器地址：" + ctx.channel().remoteAddress());
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
