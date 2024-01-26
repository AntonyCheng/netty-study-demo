package top.sharehome.chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 聊天室客户端
 *
 * @author AntonyCheng
 */

public class ChatClient01 {

    /**
     * 客户端启动方法
     */
    private static void startClient(String name) throws IOException {
        // 连接服务器
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));

        // 接收服务端的数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 创建线程
        new Thread(() -> {
            try {
                for(;;){
                    int readChannels = selector.select();
                    if (readChannels==0){
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        if (next.isReadable()) {
                            receiveMessage(next, selector);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        // 向服务器端发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (!nextLine.isEmpty()) {
                socketChannel.write(ByteBuffer.wrap((name + ":" + nextLine).getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    /**
     * 接收服务端响应的数据
     */
    private static void receiveMessage(SelectionKey selectionKey, Selector selector) throws IOException {
        // 从SelectionKey获取已经就绪的通道
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(5);
        // 循环读取客户端发来的信息
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (channel.read(buffer) > 0) {
            buffer.flip();
            byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
            buffer.clear();
        }
        String message = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        // 把通道再次注册到选择器上去，监听可读状态
        channel.register(selector, SelectionKey.OP_READ);
        // 把客户端发送的消息广播到其他客户端中去
        if (!message.isEmpty()) {
            System.out.println(message);
        }
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        startClient("01");
    }

}
