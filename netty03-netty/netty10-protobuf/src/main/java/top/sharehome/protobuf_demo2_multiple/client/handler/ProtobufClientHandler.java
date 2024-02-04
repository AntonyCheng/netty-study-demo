package top.sharehome.protobuf_demo2_multiple.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.sharehome.protobuf_demo0_init.pojo.Multiple;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 异步线程发送Boss类型，不然writeAndFlush会刷新同一片缓冲区
        ctx.channel().eventLoop().execute(()->{
            Multiple.Boss boss = Multiple.Boss.newBuilder().setId(10010).setName("AntonyCheng").build();
            Multiple.MultipleFactory bossFactory = Multiple.MultipleFactory.newBuilder().setDateType(Multiple.MultipleFactory.DateType.BossType).setBoss(boss).build();
            ctx.channel().writeAndFlush(bossFactory);
            System.out.println("已发送boss信息");
        });
        // 发送Worker类型
        Multiple.Worker worker = Multiple.Worker.newBuilder().setId(10011).setBossName("AntonyCheng").setName("Antony").build();
        Multiple.MultipleFactory workerFactory = Multiple.MultipleFactory.newBuilder().setDateType(Multiple.MultipleFactory.DateType.WorkerType).setWorker(worker).build();
        ctx.channel().writeAndFlush(workerFactory);
        System.out.println("已发送worker信息");
    }
}
