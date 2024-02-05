package top.sharehome.rpc.provider.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.sharehome.rpc.provider.impl.HelloServiceImpl;

/**
 * Netty服务端自定义处理器
 *
 * @author AntonyCheng
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理可读事件
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用服务
        System.out.println("msg: " + msg);
        // 客户端在调用服务器的服务时，需要定义一个协议让客户端遵守
        // 例如服务端要求每次发送消息时都必须以某个字符串开头（这里就以"HelloService#hello#"作为协议头进行调用）
        if (msg.toString().startsWith("HelloService#hello#")){
            // 截取出正确的参数
            String param = msg.toString().substring(msg.toString().lastIndexOf("#") + 1);
            // 调用函数获取结果
            String hello = new HelloServiceImpl().hello(param);
            // 返回结果
            ctx.channel().writeAndFlush(hello);
        }
    }

    /**
     * 处理异常事件
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.channel().close();
    }
}
