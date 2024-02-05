package top.sharehome.demo01_problem.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 * 客户端处理器
 *
 * @author AntonyCheng
 */
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 向服务端发送十次消息
        for (int i = 0; i < 10; i++) {
            ByteBuf buf = Unpooled.copiedBuffer("hello world " + i, StandardCharsets.UTF_8);
            ctx.channel().writeAndFlush(buf);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String content = msg.toString(0, msg.readableBytes(), StandardCharsets.UTF_8);
        System.out.println("来自服务端：" + content);
    }
}
