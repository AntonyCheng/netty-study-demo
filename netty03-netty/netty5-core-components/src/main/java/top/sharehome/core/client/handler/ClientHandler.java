package top.sharehome.core.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String string = buf.toString(StandardCharsets.UTF_8);
        System.out.println("来自服务器：" + string);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.print("请输入你要转换的内容：");
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.nextLine();
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(nextLine,StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接已断开...");
    }
}
