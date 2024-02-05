package top.sharehome.rpc.consumer;

import top.sharehome.rpc.common.HelloService;
import top.sharehome.rpc.consumer.client.NettyClient;

/**
 * 客户端启动类
 *
 * @author AntonyCheng
 */

public class ClientBootstrap {
    // 这里定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {
        System.out.println("开始远程调用方法...");
        // 创建一个消费者
        NettyClient consumer = new NettyClient();

        // 创建代理对象
        HelloService service = (HelloService) consumer.getBean(HelloService.class, providerName);

        // 通过代理对象调用服务提供者的方法
        String res = service.hello("你好 RPC~");
        System.out.println("调用的结果为：" + res);
    }
}
