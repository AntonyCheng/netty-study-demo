package top.sharehome.rpc.consumer.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * Netty客户端自定义处理器
 * 除了继承ChannelInboundHandlerAdapter类，还要实现Callable接口重写其call()“调用”方法
 * 方法调用顺序即编号顺序
 *
 * @author AntonyCheng
 */

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    /**
     * 参数化通道上下文，因为需要在其他方法使用到该参数
     */
    private ChannelHandlerContext ctx;

    /**
     * 调用方法得到的结果
     */
    private String result;

    /**
     * 调用方法传入的参数
     */
    private String param;

    /**
     * 1、处理活动状态就绪事件
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("正在处理活动状态就绪事件...");
        // 初始化参数ctx
        this.ctx = ctx;
    }

    /**
     * 2、获取param的方法
     */
    public void setParam(String param) {
        System.out.println("正在获取参数...");
        this.param = param;
    }

    /**
     * 3或5、重写“调用”方法
     * 该方法会被代理对象调用，主要负责给服务端发送消息，然后等待wait，被channelRead唤醒之后再返回结果
     * 所以该方法也需要synchronized修饰
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("代理对象正在发送数据到服务端...");
        // 发送给服务器的参数
        ctx.channel().writeAndFlush(param);
        // 进行等待，直到channelRead方法拿到服务器返回的接过之后
        wait();
        // 返回服务器返回的结果
        System.out.println("代理对象正在返回结果到客户端...");
        return result;
    }

    /**
     * 4、处理可读就绪事件
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("正在处理可读就绪事件...");
        // 初始化参数result
        this.result = msg.toString();
        // 唤醒等待的线程，在call中会以反射的形式调用远程服务，调用完之后会让call中的线程等待
        // 而远程服务器处理完被调用消息返回的结果是发送给channelRead信息的，
        // 所以这里初始化参数result的意义就在于让该参数能被call中的线程访问，方法中用synchronized修饰也是为了和call方法保持同步，而非异步
        // 当这里进行唤醒后，call中的线程会拿到result结果返回给调用者
        notify();
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
