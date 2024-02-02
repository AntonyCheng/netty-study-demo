package top.sharehome.chat.client01.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 多人聊天客户端处理器
 *
 * @author AntonyCheng
 */
public class ChatClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理可读就绪事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(msg);
    }

    /**
     * 一旦发生异常就关闭连接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("服务器断开连接...");
        ctx.close();
    }

}
