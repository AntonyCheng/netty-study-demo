package top.sharehome.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * SocketChannel示例代码类
 * 1、SocketChannel是用来连接Socket套接字的
 * 2、SocketChannel是主要用来处理网络IO的通道
 * 3、SocketChannel是基于TCP连接的
 * 4、SocketChannel实现了可选择通道，可以被多路复用
 *
 * @author AntonyCheng
 */

public class Demo04SocketChannel {

    /**
     * 创建SocketChannel
     */
    private static void open() throws IOException {
        // 1、直接使用open方法进行创建（常用）
        SocketChannel socketChannel1 = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));
        // 2、使用open方法创建一个空SocketChannel，然后单独使用connect方法进行连接
        SocketChannel socketChannel2 = SocketChannel.open();
        socketChannel2.connect(new InetSocketAddress("www.baidu.com", 80));
    }

    /**
     * 校验连接
     */
    private static void validate() throws IOException {
        // 创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));

        // 校验SocketChannel
        // 1、是否为open状态
        boolean isOpen = socketChannel.isOpen();
        // 2、是否连接
        boolean isConnected = socketChannel.isConnected();
        // 3、是否正在连接
        boolean isConnectionPending = socketChannel.isConnectionPending();
        // 4、是否连接完成
        boolean isFinishConnect = socketChannel.finishConnect();

        System.out.println("isOpen = " + isOpen);
        System.out.println("isConnected = " + isConnected);
        System.out.println("isConnectionPending = " + isConnectionPending);
        System.out.println("isFinishConnect = " + isFinishConnect);
    }

    /**
     * 阻塞模式转换
     */
    private static void transferBlockStatus() throws IOException {
        // 创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));
        // 选择阻塞模式
        // 1、非阻塞
        socketChannel.configureBlocking(false);
        // 2、阻塞
        socketChannel.configureBlocking(true);
    }

    /**
     * 读写示例（已读为例）
     */
    private static void read() throws IOException {
        // 创建SocketChannel（连接百度是得不到数据的，所以最后buffer中的内容为空）
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));
        // 使其非阻塞
        socketChannel.configureBlocking(false);
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 通过socketChannel读取数据到buffer中
        socketChannel.read(buffer);
        // 转换buffer写模式到读模式
        buffer.flip();
        // 获取buffer中的内容
        while (buffer.hasRemaining()){
            System.out.print((char) buffer.get());
        }
        socketChannel.close();
        System.out.println("\n读取结束");
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        read();
    }

}
