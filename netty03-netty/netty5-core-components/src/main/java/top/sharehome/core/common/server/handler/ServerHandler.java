package top.sharehome.core.common.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端处理器
 * 以下的方法顺序就是数据在通道中的生命周期
 *
 * @author AntonyCheng
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 1、处理通道开启连接事件
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道开启连接事件");
    }

    /**
     * 2、处理通道注册事件
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道注册事件");
    }

    /**
     * 3、处理通道处于活动事件
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道活动状态事件");
    }

    /**
     * 4、处理通道可读事件
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("通道可读事件");
        ctx.channel().writeAndFlush("被读取数据为“" + msg + "”");
        // 也可以这么写消息，但是为了在编码层面体现该动作是由channel发起的，所以不建议这么写
        //ctx.writeAndFlush("被读取数据为“" + msg + "”");
    }

    /**
     * 5、处理通道读完事件
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道读完事件");
    }

    /**
     * 6、处理通道处于非活动状态事件
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道非活动状态事件");
    }

    /**
     * 7、处理通道注销事件
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道注销事件");
    }

    /**
     * 8、处理通道断开连接事件
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("通道断开连接事件");
    }

    /**
     * 通道发生异常事件（生命周期中实时监控）
     * 一旦发生异常，直接断开连接即可
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("通道发生异常");
        // 断开连接
        ctx.close();
    }

}
