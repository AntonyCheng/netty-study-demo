package top.sharehome.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.internal.ChannelUtils;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Http服务器处理器
 * SimpleChannelInboundHandler是ChannelInboundHandlerAdapter子类
 *
 * @author AntonyCheng
 */

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端的数据
     * HttpObject指的是客户端和服务器端相互通讯的数据封装后的数据类型
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 判断httpObject是不是httpRequest请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("客户端地址：" + channelHandlerContext.channel().remoteAddress());
            // 过滤特定资源
            HttpRequest httpRequest = (HttpRequest) httpObject;
            String uri = httpRequest.uri();
            if ("/favicon.ico".equals(uri)) {
                System.out.println("对" + uri + "不做响应");
                return;
            }
            // 回复信息给浏览器 ==> 信息需要满足Http协议
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello,我是服务器", StandardCharsets.UTF_8);
            // 构造一个http的响应
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            HttpHeaders headers = response.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            headers.set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            // 将构建好的response返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
