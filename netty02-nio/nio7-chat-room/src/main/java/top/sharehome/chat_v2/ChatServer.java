package top.sharehome.chat_v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

/**
 * 聊天室服务器端
 *
 * @author AntonyCheng
 */

public class ChatServer {
    // 定义相关的属性
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private final int PORT = 9999;

    // 初始化服务器
    public ChatServer() {
        try {
            // 初始化selector
            selector = Selector.open();
            // 初始化ServerSocketChannel
            serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(PORT));
            // 开启非阻塞
            serverSocketChannel.configureBlocking(false);
            // 将ServerSocketChannel注册进Selector中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 服务器启动完成提示
            System.out.println("服务器启动完成...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 监听
    public void listen() {
        try {
            while (selector.select() > 0) {
                // 说明有就绪状态到达，就获取选择键
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 获取迭代器
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // 遍历迭代器
                while (iterator.hasNext()) {
                    // 获取到单个迭代元素
                    SelectionKey next = iterator.next();
                    // 如果是接收就绪状态，就获取连接
                    if (next.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        // 配置非阻塞
                        socketChannel.configureBlocking(false);
                        // 以可读就绪状态注册到Selector
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        // 上线提示
                        System.out.println(socketChannel.getRemoteAddress() + "上线了！");
                        socketChannel.write(StandardCharsets.UTF_8.encode("您已上线，请注意保护个人隐私"));
                    }
                    // 如果是可读就绪状态
                    if (next.isReadable()) {
                        // 从选择键中获取Channel
                        SocketChannel channel = (SocketChannel) next.channel();
                        // 处理读取数据
                        readData(next);
                    }
                    // 处理完之后移除当前选择键避免重复操作
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 发生异常最后的处理
            String msg = "服务器异常，连接已断开";
            System.out.println(msg);
            Iterator<SelectionKey> iterator = selector.keys().iterator();
            while (iterator.hasNext()) {
                try {
                    SelectableChannel channel = iterator.next().channel();
                    if (channel instanceof SocketChannel) {
                        ((SocketChannel) channel).write(StandardCharsets.UTF_8.encode(msg));
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // 读取客户端消息
    private void readData(SelectionKey selectionKey) {
        // 定义一个SocketChannel
        SocketChannel socketChannel = null;
        try {
            // 获取到关联的Channel
            socketChannel = (SocketChannel) selectionKey.channel();
            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 读取Channel中的值到缓冲区，并形成消息
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }
            String message = LocalDateTime.now() + "==>" + new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
            // 在服务端进行消息展示
            System.out.println(message);
            // 向其他客户端转发消息
            castToOther(message, socketChannel);
        } catch (IOException e) {
            try {
                // 如果读到消息出现了异常就代表连接断开，客户端离线，这里进行打印操作
                System.out.println(socketChannel.getRemoteAddress() + "离线了...");
                // 然后取消选择键的注册
                selectionKey.channel();
                // 最后关闭通道
                socketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 转发消息
    private void castToOther(String message, SocketChannel self) {
        try {
            Set<SelectionKey> selectionKeySet = selector.keys();
            for (SelectionKey selectionKey : selectionKeySet) {
                // 取出通道
                Channel channel = selectionKey.channel();
                // 确定类型以及排除自己
                if (channel instanceof SocketChannel && channel != self) {
                    ((SocketChannel) channel).write(StandardCharsets.UTF_8.encode(message));
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
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }

}
