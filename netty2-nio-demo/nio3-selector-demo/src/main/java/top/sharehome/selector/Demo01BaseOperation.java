package top.sharehome.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Selector基础操作
 * 注意：
 * 1、与Selector一起使用时，Channel必须处于非阻塞模式下，否则将抛出异常IllegalBlockingModeException。
 * 这意味着，FileChannel不能与Selector一起使用，因为FileChannel不能切换到非阻塞模式，而套接字相关的所有的通道都可以。
 * 2、一个通道，并没有一定要支持所有的四种操作。比如服务器通道ServerSocketChannel支持Accept接受操作，而SocketChannel客户端通道则不支持。
 * 可以通过通道上的validOps()方法，来获取特定通道下所有支持的操作集合。
 *
 * @author AntonyCheng
 */

public class Demo01BaseOperation {

    /**
     * 将Channel注册到Selector上
     */
    public static void main(String[] args) throws IOException {

        // 创建Selector
        Selector selector = Selector.open();

        // 创建一个通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 使用非阻塞
        serverSocketChannel.configureBlocking(false);

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9999));

        // 将通道注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

    }

}
