package top.sharehome.rpc.provider;

import top.sharehome.rpc.provider.server.NettyServer;

/**
 * 服务端启动类
 * todo:研究一下sync()方法到底有什么作用
 *
 * @author AntonyCheng
 */

public class ServerBootstrap {
    /**
     * ServerBootstrap类会启动一个服务提供者
     */
    public static void main(String[] args) {
        System.out.println("正在启动服务提供者...");
        NettyServer.startServer("127.0.0.1", 9999);
    }
}
