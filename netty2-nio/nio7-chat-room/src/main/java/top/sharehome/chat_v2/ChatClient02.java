package top.sharehome.chat_v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 聊天室客户端
 *
 * @author AntonyCheng
 */

public class ChatClient02 {
    // 定义相关属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 9999;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 初始化客户端
    public ChatClient02() {
        try {
            // 初始化selector
            selector = Selector.open();
            // 初始化SocketChannel，连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 开启非阻塞
            socketChannel.configureBlocking(false);
            // 将SocketChannel注册进Selector中
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 初始化用户名
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + "客户端初始化成功...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 向服务器发送消息
    public void send(String message) throws IOException {
        if (message.trim().isEmpty()){
            return;
        }
        message = username + " : " + message;
        socketChannel.write(StandardCharsets.UTF_8.encode(message));
    }

    // 读取服务器端回复的消息
    public void read() {
        try {
            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if (next.isReadable()) {
                        SocketChannel channel = (SocketChannel) next.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        while (channel.read(buffer) > 0) {
                            buffer.flip();
                            byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
                            buffer.clear();
                        }
                        String message = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
                        System.out.println(message);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        ChatClient02 chatClient = new ChatClient02();
        // 启动线程进行读取数据
        new Thread(chatClient::read).start();
        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String str = scanner.nextLine();
            try {
                chatClient.send(str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
