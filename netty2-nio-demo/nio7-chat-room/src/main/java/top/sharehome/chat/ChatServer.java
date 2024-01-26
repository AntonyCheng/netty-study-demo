package top.sharehome.chat;

import io.netty.util.internal.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 聊天室服务器端
 *
 * @author AntonyCheng
 */

public class ChatServer {

    // 服务器端启动方法
    private static void startServer() throws IOException {
        // 1、创建Selector选择器
        Selector selector = Selector.open();

        // 2、创建非阻塞ServerSocketChannel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // 3、为Channel通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(9999));

        // 4、循环，等待是否有新的连接接入
        // 把Channel注册到Selector中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动成功...");

        // 5、循环，等待有新连接接入
        while (selector.select() > 0) {
            // 根据就绪状态来调用对应方法实现具体业务操作
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    acceptStatus(serverSocketChannel, selector);
                } else if (next.isReadable()) {
                    readStatus(next, selector);
                }
                // 每次一定要移除迭代器next元素
                iterator.remove();
            }
        }
    }

    /**
     * 定义接收就绪状态的操作
     * 如果是接收就绪状态，就将其转为可读就绪状态
     */
    private static void acceptStatus(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        // 接入状态，创建SocketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 设置成非阻塞
        socketChannel.configureBlocking(false);
        // 把Channel注册到Selector选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 给客户端回复提示信息
        socketChannel.write(ByteBuffer.wrap("您已进入聊天室，请注意隐私安全！".getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 定义可读就绪状态的操作
     */
    private static void readStatus(SelectionKey selectionKey, Selector selector) throws IOException {
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
            String result = LocalDateTime.now() + "###" + channel.getRemoteAddress() + " ==> " + message;
            System.out.println(result);
            // 广播给其他客户端
            castOtherClient(LocalDateTime.now() + " ==> " + message, selector, channel);
        }
    }

    /**
     * 广播给其他客户端
     */
    private static void castOtherClient(String result, Selector selector, SocketChannel channel) throws IOException {
        // 获取所有已经接入的Channel
        Set<SelectionKey> selectionKeySet = selector.keys();
        // 循环向其他所有channel发送消息
        for (SelectionKey selectionKey : selectionKeySet) {
            Channel otherChannel = selectionKey.channel();
            // 不需要给自己发消息
            if (otherChannel instanceof SocketChannel && otherChannel != channel) {
                ((SocketChannel) otherChannel).write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
            }
        }
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        startServer();
    }

}
