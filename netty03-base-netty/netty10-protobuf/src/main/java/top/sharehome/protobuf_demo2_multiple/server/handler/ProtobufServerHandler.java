package top.sharehome.protobuf_demo2_multiple.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.sharehome.protobuf_demo0_init.pojo.Multiple;

/**
 * 客户端处理器
 *
 * @author AntonyCheng
 */
public class ProtobufServerHandler extends SimpleChannelInboundHandler<Multiple.MultipleFactory> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Multiple.MultipleFactory msg) throws Exception {
        // 根据DateType显示不同的信息
        Multiple.MultipleFactory.DateType dateType = msg.getDateType();
        if (dateType.equals(Multiple.MultipleFactory.DateType.BossType)) {
            System.out.println("boss id=" + msg.getBoss().getId() + " name=" + msg.getBoss().getName());
        } else if (dateType.equals(Multiple.MultipleFactory.DateType.WorkerType)) {
            System.out.println("worker id=" + msg.getWorker().getId() + " name=" + msg.getWorker().getName() + " bossName=" + msg.getWorker().getBossName());
        } else {
            System.out.println("无效的类型");
        }
    }
}