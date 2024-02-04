package top.sharehome.protobuf_demo1_single.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.sharehome.protobuf_demo0_init.pojo.Single;

/**
 * 客户端处理器
 *
 * @author AntonyCheng
 */
public class ProtobufServerHandler extends SimpleChannelInboundHandler<Single.Student> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Single.Student msg) throws Exception {
        System.out.println("客户端发送的数据 id=" + msg.getId() + " name=" + msg.getName());
    }
}