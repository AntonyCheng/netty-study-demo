package top.sharehome.demo02_solve.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.sharehome.demo02_solve.MessageProtocol;

import java.nio.charset.StandardCharsets;

/**
 * 客户端处理器
 *
 * @author AntonyCheng
 */
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 用客户端发送5条数据
        for (int i = 0; i < 5; i++) {
            String msg = "你好！the " + (i + 1) + " times！";
            byte[] content = msg.getBytes(StandardCharsets.UTF_8);
            int length = content.length;
            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            // 发送协议包
            ctx.channel().writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 拿到内容长度
        int length = msg.getLen();
        // 获取内容
        String content = new String(msg.getContent(), StandardCharsets.UTF_8);
        System.out.println("来自服务端：" + content + "\t消息长度为：" + length);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息：" + cause.getMessage());
        ctx.channel().close();
    }
}
