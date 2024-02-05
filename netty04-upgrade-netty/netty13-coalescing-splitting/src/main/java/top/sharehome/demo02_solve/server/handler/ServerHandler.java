package top.sharehome.demo02_solve.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.sharehome.demo02_solve.MessageProtocol;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 服务端处理器
 *
 * @author AntonyCheng
 */
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    /**
     * 记录执行次数
     */
    private Integer count = 0;

    /**
     * 可读就绪事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 拿到内容长度
        int length = msg.getLen();
        // 获取内容
        String content = new String(msg.getContent(), StandardCharsets.UTF_8);
        System.out.println("来自客户端：" + content + "\t消息长度为：" + length);
        System.out.println("调用可读就绪事件处理方法次数：" + (++this.count));
        // 再生成一个UUID，回写客户端
        String string = UUID.randomUUID().toString();
        String message = this.count + " ==> " + string;
        byte[] sendContent = message.getBytes(StandardCharsets.UTF_8);
        int sendLength = sendContent.length;
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(sendLength);
        messageProtocol.setContent(sendContent);
        ctx.channel().writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息：" + cause.getMessage());
        ctx.channel().close();
    }
}
