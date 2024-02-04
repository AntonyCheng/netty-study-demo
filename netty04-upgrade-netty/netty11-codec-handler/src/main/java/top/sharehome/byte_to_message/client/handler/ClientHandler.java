package top.sharehome.byte_to_message.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * 客户端处理器
 *
 * @author AntonyCheng
 */
public class ClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(LocalDateTime.now() + "客户端正在发送数据...");
        // 发送一个Long型
        ctx.channel().writeAndFlush(10086L);
        // 如果发送长度为16的字符串，那么会出现如下情况：
        // 1、编码器不会承认非Long类型的数据，所以编码器会被直接跳过，将数据不经过处理发送给服务端。
        // 2、服务端拿到数据之后会将数据进行解码，但是此时有16个字节，即使依旧被正常解码，但需要被解码两次，也就是说客户端需要执行两次“解码+接收”的操作。
        //ctx.channel().writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", StandardCharsets.UTF_8));
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println(LocalDateTime.now() + "客户端正在接收数据...");
        System.out.println("来自服务端：" + msg);
    }
}
