package top.sharehome.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 创建一个服务器，能够不断接收数据
 *
 * @author AntonyCheng
 */
public class Demo03SuperServer {

    public static void main(String[] args) throws IOException {
        // 1、创建通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 2、切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 3、创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 3、绑定端口号
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9999));

        // 4、获取Selector选择器
        Selector selector = Selector.open();

        // 5、通道注册到选择器
        serverSocketChannel.register(selector, serverSocketChannel.validOps());

        System.out.println("服务器启动成功...");

        // 6、选择器进行轮询进行后续操作
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    // 先获取对象
                    SocketChannel accept = serverSocketChannel.accept();
                    // 转变为非阻塞
                    accept.configureBlocking(false);
                    // 注册到选择器中
                    accept.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 从key中获取通道
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 使用Buffer进行读取
                    while (channel.read(buffer) != -1) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            System.out.println(StandardCharsets.UTF_8.decode(buffer));
                        }
                        buffer.clear();
                    }
                    channel.close();
                }
                // 每次一定要移除迭代器的next元素
                iterator.remove();
            }
        }
    }

}
