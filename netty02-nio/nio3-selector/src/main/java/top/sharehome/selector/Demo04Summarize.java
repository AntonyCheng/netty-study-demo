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
 * NIO总结
 * 1、创建ServerSocketChannel通道，绑定监听端口
 * 2、设置通道为非阻塞模式
 * 3、创建Selector选择器
 * 4、吧Channel注册到Selector选择器上，监听连接事件
 * 5、调用Selector的select方法（循环调用），监测通道的就绪状态
 * 6、调用SelectKeys方法获取就绪Channel集合
 * 7、遍历就绪Channel集合，判断就绪事件类型，然后实现具体业务代码的操作
 * 8、根据业务流程是否还需要注册监听事件，如果需要，就重复执行3~8步的操作
 * 接下来将总结和异步线程相结合写一个客户端输入英文，服务端转换大小写的程序
 *
 * @author AntonyCheng
 */
public class Demo04Summarize {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 首先开启服务端
        new Thread(() -> {
            try {
                // 创建服务端通道
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                // 开启非阻塞
                serverSocketChannel.configureBlocking(false);
                // 服务端绑定地址和端口
                serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9999));
                // 创建服务端Buffer
                ByteBuffer serverBuffer = ByteBuffer.allocate(1024);
                System.out.println("服务端启动完毕...");
                // 创建Selector
                Selector selector = Selector.open();
                // 注册通道到Selector
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                // 循环判断Selector是否有感兴趣的就绪状态
                while (selector.select() > 0) {
                    // 获取选择键的迭代对象
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    // 挨个判断迭代键属于那种就绪状态
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        // 针对接收就绪状态，将其注册成为可读就绪状态
                        if (next.isAcceptable()) {
                            // 获取该选择键所指向的通道
                            SocketChannel channel = serverSocketChannel.accept();
                            // 开启非阻塞
                            channel.configureBlocking(false);
                            // 将该通道注册以可读就绪模式注册进Selector中
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (next.isReadable()) {
                            // 获取该选择键所指向的通道，使用Buffer讲内容读取出来
                            SocketChannel channel = (SocketChannel) next.channel();
                            channel.read(serverBuffer);
                            // 处理severBuffer中的字符，进行大小写转换
                            serverBuffer.flip();
                            while (serverBuffer.hasRemaining()) {
                                byte b = serverBuffer.get();
                                if (b < 'a') {
                                    b = (byte) (b + 32);
                                } else {
                                    b = (byte) (b - 32);
                                }
                                System.out.print((char) b);
                            }
                            System.out.println();
                            // 清空buffer
                            serverBuffer.clear();
                        }
                        // 每次一定要移除迭代器next元素
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // 两秒后开启客户端通道
        Thread.sleep(2000);
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        // 开启非阻塞
        clientChannel.configureBlocking(false);
        // 创建客户端Buffer
        ByteBuffer clientBuffer = ByteBuffer.allocate(1024);
        // 获取Scanner
        Scanner scanner = new Scanner(System.in);
        System.out.println("客户端启动完毕...");
        while (scanner.hasNext()) {
            String next = scanner.next();
            clientBuffer.put(next.getBytes(StandardCharsets.UTF_8));
            clientBuffer.flip();
            clientChannel.write(clientBuffer);
            clientBuffer.clear();
        }

    }

}
