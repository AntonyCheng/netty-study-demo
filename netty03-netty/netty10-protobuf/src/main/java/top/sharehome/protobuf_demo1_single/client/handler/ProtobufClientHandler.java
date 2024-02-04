package top.sharehome.protobuf_demo1_single.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.sharehome.protobuf_demo0_init.pojo.Single;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送类型
        Single.Student student = Single.Student.newBuilder().setId(10010).setName("AntonyCheng").build();
        ctx.channel().writeAndFlush(student);
    }
}
