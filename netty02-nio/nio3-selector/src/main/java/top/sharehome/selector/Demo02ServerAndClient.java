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
import java.util.Scanner;

/**
 * 服务端和客户端代码
 *
 * @author AntonyCheng
 */
public class Demo02ServerAndClient {

    /**
     * 客户端代码
     */
    private static void client() throws IOException {
        // 1、创建通道，绑定主机和端口号
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));

        // 2、切换到非阻塞模式
        socketChannel.configureBlocking(false);

        // 3、创建读模式Buffer
        ByteBuffer buffer = ByteBuffer.wrap("你好！世界！".getBytes(StandardCharsets.UTF_8));

        // 4、读取buffer数据写入通道
        socketChannel.write(buffer);

        // 5、关闭通道
        socketChannel.close();
    }

    /**
     * 服务端代码
     */
    private static void server() throws IOException {
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
                        System.out.println(StandardCharsets.UTF_8.decode(buffer));
                        buffer.clear();
                    }
                    channel.close();
                }
                // 每次一定要移除迭代器next元素
                iterator.remove();
            }
        }
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        // 异步调用方法执行客户端和服务端的交互
        new Thread(() -> {
            try {
                server();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(2000);
        client();

        System.in.read();
    }

}
