package top.sharehome.protobuf_demo2_multiple.client.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.sharehome.protobuf_demo0_init.pojo.Multiple;

import java.util.concurrent.TimeUnit;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 异步延迟0.1秒线程发送Boss类型，不然writeAndFlush会刷新同一片缓冲区，如果不想延时发送，那就写两个Handler
        ctx.channel().eventLoop().schedule(() -> {
            Multiple.Boss boss = Multiple.Boss.newBuilder().setId(10010).setName("AntonyCheng").build();
            Multiple.MultipleFactory bossFactory = Multiple.MultipleFactory.newBuilder().setDateType(Multiple.MultipleFactory.DateType.BossType).setBoss(boss).build();
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(bossFactory);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("已发送boss信息");
                    }
                }
            });
        }, 100, TimeUnit.MILLISECONDS);
        // 发送Worker类型
        Multiple.Worker worker = Multiple.Worker.newBuilder().setId(10011).setBossName("AntonyCheng").setName("Antony").build();
        Multiple.MultipleFactory workerFactory = Multiple.MultipleFactory.newBuilder().setDateType(Multiple.MultipleFactory.DateType.WorkerType).setWorker(worker).build();
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(workerFactory);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("已发送worker信息");
                }
            }
        });
    }
}
