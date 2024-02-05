package top.sharehome.demo01_problem.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    /**
     * 记录执行次数
     */
    private Integer count = 0;

    /**
     * 可读就绪事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        String content = msg.toString(StandardCharsets.UTF_8);
        System.out.println("来自客户端：" + content);
        System.out.println("调用可读就绪事件处理方法次数：" + (++this.count));
        System.out.println("正在回写随机数...");
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString(), StandardCharsets.UTF_8));
    }
}
