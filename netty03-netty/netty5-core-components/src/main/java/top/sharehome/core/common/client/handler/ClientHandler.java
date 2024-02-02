package top.sharehome.core.common.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端处理器
 * 客户端一般就只处理以下事件
 *
 * @author AntonyCheng
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 处理通道可读事件
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("来自服务器：" + msg);
    }

    /**
     * 处理通道读完事件
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道读完事件");
    }

    /**
     * 通道发生异常事件
     * 一旦发生异常，直接断开连接即可
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("通道发生异常");
        // 断开连接
        ctx.close();
    }
}
