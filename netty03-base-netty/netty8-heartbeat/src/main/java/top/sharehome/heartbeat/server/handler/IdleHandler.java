package top.sharehome.heartbeat.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳（空闲）检测处理器
 * 由于不需要处理可读就绪事件，所以这里继承ChannelInboundHandlerAdapter即可
 *
 * @author AntonyCheng
 */
public class IdleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断该事件是否是心跳触发事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 创建事件类型名称（用于示例打印使用）
            String eventType = null;
            // 使用switch...case...结构进行选择（因为选择的是枚举，switch...case...更加美观）
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress().toString()+"---心跳超时事件---"+eventType);
            // 接下来就是进行处理，这个和业务逻辑强绑定，这里就举一个例子，就是一旦超时就直接关闭通道
            //ctx.channel().close();
            System.out.println("服务器正在做出对应的处理...");
        }
    }
}
