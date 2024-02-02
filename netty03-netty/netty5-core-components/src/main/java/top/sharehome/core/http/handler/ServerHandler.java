package top.sharehome.core.http.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * 服务端处理器
 * 以下的方法顺序就是数据在通道中的生命周期
 *
 * @author AntonyCheng
 */
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 1、处理通道开启连接事件
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道开启连接事件");
    }

    /**
     * 2、处理通道注册事件
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道注册事件");
    }

    /**
     * 3、处理通道处于活动事件
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道活动状态事件");
    }

    /**
     * 4、处理通道可读事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("通道可读事件");
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            System.out.println("读取到的请求体URI是：" + request.uri());
            ByteBuf content = Unpooled.copiedBuffer("这是响应给客户端的数据", StandardCharsets.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                    // 设置 HTTP 版本
                    HttpVersion.HTTP_1_1,
                    // 设置响应状态码
                    HttpResponseStatus.OK,
                    // 设置相应内容
                    content,
                    // 设置响应头
                    new DefaultHttpHeaders() {
                        {
                            add(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                            add(HttpHeaderNames.CONTENT_LENGTH, content.array().length);
                        }
                    },
                    // 设置响应尾部（可以忽略）
                    new DefaultHttpHeaders()
            );
            // 响应内容
            ctx.channel().writeAndFlush(response);
            // 这样也能响应内容，但是为了在编码层面体现该动作是由channel发起的，所以不建议这么写
            //ctx.writeAndFlush(response);
        }
    }

    /**
     * 5、处理通道读完事件
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道读完事件");
    }

    /**
     * 6、处理通道处于非活动状态事件
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道非活动状态事件");
    }

    /**
     * 7、处理通道注销事件
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道注销事件");
    }

    /**
     * 8、处理通道断开连接事件
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道断开连接事件");
    }

    /**
     * 通道发生异常事件（生命周期中实时监控）
     * 一旦发生异常，直接断开连接即可
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("通道发生异常");
        // 断开连接
        ctx.close();
    }
}
