package top.sharehome.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Http服务器处理器初始化
 *
 * @author AntonyCheng
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个netty提供的httpServerCodec编码器，既能编码也能解码
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        // 加入对Web请求处理的处理器
        pipeline.addLast("HttpServerHandler", new HttpServerHandler());
    }

}
