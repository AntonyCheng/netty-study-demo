package top.sharehome.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * DatagramChannel示例代码类
 * DatagramChannel和SocketChannel的主要区别就是DatagramChannel是面向UDP协议的，它是一种无连接的状态，既可以发送信息到任何地址，也可以接收来自任何地址的信息。
 *
 * @author AntonyCheng
 */
public class Demo05DatagramChannel {

    /**
     * 创建DatagramChannel
     */
    private static void open() throws IOException {
        // 使用open方法创建一个DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 再调用socket方法得到DatagramSocket，之后调用bind方法进行端口绑定
        datagramChannel.socket().bind(new InetSocketAddress("127.0.0.1", 10086));
        // 此时就能通过127.0.0.1:10086进行收发数据
    }

    /**
     * 接收数据
     */
    private static void receive() throws IOException {
        // 1、首先需要创建一个DatagramChannel
        // 使用open方法创建一个DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 再调用socket方法得到DatagramSocket，之后调用bind方法进行端口绑定
        datagramChannel.socket().bind(new InetSocketAddress("127.0.0.1", 10086));

        // 2、然后创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 调用receive方法通过127.0.0.1:10086进行接收数据，数据写入Buffer中
        SocketAddress receive = datagramChannel.receive(buffer);
        System.out.println(receive.toString());
    }

    /**
     * 发送数据（可与write方法搭配使用，但是需要在不同线程下进行测试）
     */
    private static void send() throws IOException {
        // 1、首先需要创建一个DatagramChannel
        // 使用open方法创建一个DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 2、创建读模式Buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello!!!".getBytes());
        // 3、发送数据
        int send = datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 10086));
        System.out.println(send);
    }

    /**
     * 伪“连接”，对于UDP就是无连接的状态，这里的连接只是从API层面对其进行描述
     * 这里就是使用connect方法之后对特定服务地址使用read和write接收和发送数据
     * DatagramChannel中的read()方法如果没读到数据，返回的是0，而FileChannel返回的是-1
     */
    private static void readAndWritePseudoConnect() throws IOException {
        // 使用open方法创建一个DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(10099)).connect(new InetSocketAddress("127.0.0.1", 10099));
        new Thread(() -> {
            while (true) {
                // 创建一个Buffer作为发送数据的介质，为了避免中文乱码，需要指定编码字符集
                ByteBuffer writeBuffer = ByteBuffer.wrap(("发送时间：" + LocalDateTime.now() + " !").getBytes(StandardCharsets.UTF_8));
                try {
                    datagramChannel.write(writeBuffer);
                    Thread.sleep(1000L);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        // 创建一个Buffer进行存放接收的数据
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (true) {
            datagramChannel.read(readBuffer);
            // 读取完之后改变buffer，从写模式转变为读模式
            readBuffer.flip();
            while (readBuffer.hasRemaining()) {
                System.out.print(StandardCharsets.UTF_8.decode(readBuffer));
            }
            System.out.println();
            readBuffer.clear();
        }
    }

    /**
     * 下面演示一个服务端发送，一个客户端接收的例子
     */
    // 发送消息
    public static void sendDatagram() throws IOException, InterruptedException {
        // 创建DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();
        while (true) {
            // 为了避免中文乱码，需要指定编码字符集
            ByteBuffer buffer = ByteBuffer.wrap(("发送时间：" + LocalDateTime.now() + " !").getBytes(StandardCharsets.UTF_8));
            datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 10086));
            Thread.sleep(1000);
        }
    }

    // 接收消息
    public static void receiveDatagram() throws IOException {
        // 创建DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress("127.0.0.1", 10086));
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            datagramChannel.receive(buffer);
            buffer.flip();
            while (buffer.hasRemaining()) {
                // 按照UTF-8进行解码打印
                System.out.print(StandardCharsets.UTF_8.decode(buffer));
            }
            System.out.println();
            buffer.clear();
        }
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException, InterruptedException {
//        new Thread(() -> {
//            try {
//                receive();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//        Thread.sleep(2000);
//        send();

        readAndWritePseudoConnect();

//        new Thread(() -> {
//            try {
//                receiveDatagram();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//        Thread.sleep(2000);
//        sendDatagram();
    }

}
