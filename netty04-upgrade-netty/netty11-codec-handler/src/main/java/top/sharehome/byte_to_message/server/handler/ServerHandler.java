package top.sharehome.byte_to_message.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println(LocalDateTime.now() + "服务端正在接收数据...");
        System.out.println("来自客户端：" + msg+"，正在将这个数据转发至客户端...");
        // 向客户端发送数据
        System.out.println(LocalDateTime.now() + "服务端正在发送数据...");
        ctx.channel().writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.channel().close();
    }
}
