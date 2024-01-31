package top.sharehome.core.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("你好，我能够将你发送的英文进行大小写转换！",StandardCharsets.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        char[] charArray = buf.toString(StandardCharsets.UTF_8).toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (!(charArray[i] <= 'z' && charArray[i] >= 'a') && !(charArray[i] <= 'Z' && charArray[i] >= 'A')) {
                continue;
            }
            if (charArray[i] > 'Z') {
                charArray[i] -= 32;
            } else {
                charArray[i] += 32;
            }
        }
        String fromClient = new String(charArray);
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(fromClient, StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接已断开...");
    }
}
