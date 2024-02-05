package top.sharehome.log4j.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * Http服务端处理器
 *
 * @author AntonyCheng
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            String uri = httpRequest.uri();
            ByteBuf content = Unpooled.copiedBuffer(uri, StandardCharsets.UTF_8);
            HttpResponse httpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    content,
                    new DefaultHttpHeaders()
                            .add(HttpHeaderNames.CONTENT_TYPE, "text/plain")
                            .add(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes()),
                    new DefaultHttpHeaders()
            );
            ctx.channel().writeAndFlush(httpResponse);
        }
    }
}
