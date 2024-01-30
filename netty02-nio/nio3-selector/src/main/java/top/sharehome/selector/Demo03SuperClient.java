package top.sharehome.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 创建一个客户端，能够不断发送
 * @author AntonyCheng
 */
public class Demo03SuperClient {

    public static void main(String[] args) throws IOException {
        // 1、创建通道，绑定主机和端口号
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        // 2、切换到非阻塞模式
        socketChannel.configureBlocking(false);
        // 3、创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put(next.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            // 4、读取buffer数据写入通道
            socketChannel.write(buffer);
            buffer.clear();
        }
    }

}
