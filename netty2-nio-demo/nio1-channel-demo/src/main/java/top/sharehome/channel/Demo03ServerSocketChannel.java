package top.sharehome.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * ServerSocketChannel示例代码类
 * 这个类本身没有数据传输的功能，它是一个基于通道的监听器：
 * 1、监听连接；
 * 2、监听SocketChannel对象的创建。
 *
 * @author AntonyCheng
 */

public class Demo03ServerSocketChannel {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 设置端口号
        int port = 8888;
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());

        // 创建ServerSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 通过ServerSocketChannel创建SocketChannel，然后绑定端口号
        ServerSocket socket = ssc.socket();
        socket.bind(new InetSocketAddress(port));

        // 设置非阻塞模式
        ssc.configureBlocking(true);

        // 使用ServerSocketChannel监听是否有新的连接传入，每个连接都会创建一个SocketChannel对象，所以同时监听对象创建
        System.out.println("waiting for connection...");
        while (true) {
            // accept()方法会返回一个SocketChannel对象，这个对象如果为null，则表示没有连接，如果不为空，则表示有连接传入
            // 如果是阻塞模式下，accept()方法默认阻塞，需要有连接传入之后才能进行下一步
            SocketChannel socketChannel = ssc.accept();
            if (socketChannel == null) {
                System.out.println("null");
                Thread.sleep(2000);
            } else {
                System.out.println("connection is coming from:" + socketChannel.socket().getRemoteSocketAddress());
                // 将当前位置指向0，表示要开始操作
                buffer.rewind();
                socketChannel.write(buffer);
                socketChannel.close();
            }
        }

    }

}
