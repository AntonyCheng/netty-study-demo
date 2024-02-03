package top.sharehome.websocket.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * WebSocket处理器
 * 继承SimpleChannelInboundHandler以TextWebSocketFrame类型数据进行交互
 *
 * @author AntonyCheng
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 处理Web客户端连接事件
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // id有两种形式：LongText（唯一的）和ShortText（不一定唯一）
        System.out.println("客户端与服务端已连接：" + ctx.channel().id().asLongText());
    }

    /**
     * 处理可读就绪事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        System.out.println("服务器端收到消息：" + msg.text());
        // 回复客户端
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端已经收到消息：" + msg.text() + "\t返回服务器时间：" + LocalDateTime.now()));
    }

    /**
     * 处理Web客户端断开连接事件
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("客户端与服务端已断开连接：" + ctx.channel().id().asLongText());
    }

    /**
     * 处理发生异常事件
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("发生异常：" + cause.getMessage());
        ctx.close();
    }
}
