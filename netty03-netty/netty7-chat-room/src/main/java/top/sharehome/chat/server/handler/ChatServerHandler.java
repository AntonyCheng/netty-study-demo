package top.sharehome.chat.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 多人聊天服务端处理器
 *
 * @author AntonyCheng
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 定义一个Channel组，管理所有的Channel
     * GlobalEventExecutor.INSTANCE是一个全局实例执行器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 设置时间格式
     */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 处理连接就绪事件
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString();
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(Unpooled.copiedBuffer(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "您已进入聊天室，请注意隐私保护...", StandardCharsets.UTF_8));
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "[" + address + "]" + "已上线...");
                    // 将上线消息进行群发
                    channelGroup.writeAndFlush(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "[" + address + "]" + "进入聊天室！");
                    // 加入Channel组
                    channelGroup.add(channel);
                }
            }
        });
    }

    /**
     * 处理可读就绪事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        // 群发消息，但是排除自己
        channelGroup.forEach(channel -> {
            if (channel != ctx.channel()) {
                channel.writeAndFlush(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "[" + ctx.channel().remoteAddress().toString() + "]：" + msg);
            } else {
                ctx.channel().writeAndFlush(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "[你]："+msg);
            }
        });
    }

    /**
     * 处理断开连接就绪事件
     * 此时不需要将Channel从channel组中移除，因为调用handlerRemoved之后，Netty会自动将通道移除
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = channel.remoteAddress().toString();
        System.out.println(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "[" + address + "]" + "已离线...");
        // 将离线消息进行群发
        channelGroup.writeAndFlush(SIMPLE_DATE_FORMAT.format(new Date()) + "\t" + "[" + address + "]" + "退出聊天室！");
    }

    /**
     * 一旦发生异常就关闭连接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
