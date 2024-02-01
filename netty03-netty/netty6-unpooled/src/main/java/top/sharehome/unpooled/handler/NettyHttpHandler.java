package top.sharehome.unpooled.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * 基于 Netty 实现的 Http 服务器自定义处理器
 *
 * @author AntonyCheng
 */
public class NettyHttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri();
            System.out.println(uri);
            ByteBuf content = Unpooled.copiedBuffer("hello world", StandardCharsets.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.channel().writeAndFlush(response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }
}
