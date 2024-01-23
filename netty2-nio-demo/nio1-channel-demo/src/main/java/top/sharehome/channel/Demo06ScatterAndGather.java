package top.sharehome.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * Scatter & Gather示例代码类
 * scatter(分散)：一个Channel中的数据写到多个Buffer中去；
 * gather(聚集)：多个Buffer中的数据写到一个Channel中去；
 *
 * @author AntonyCheng
 */
public class Demo06ScatterAndGather {

    /**
     * Scatter 分散
     * 就是由多个Buffer组成一个Buffer数组，然后使用Channel的read方法读取通道数据依次写入数组各个Buffer中（上一个Buffer写满之后再写下一个）
     */
    private static void scatter() throws IOException {
        // 创建Channel
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(8000)).connect(new InetSocketAddress("127.0.0.1", 8000));

        // 初始化Channel元数据
        ByteBuffer sendBuffer = ByteBuffer.wrap("hello world!".getBytes(StandardCharsets.UTF_8));
        datagramChannel.write(sendBuffer);

        // 写入各个Buffer
        ByteBuffer readBuffer1 = ByteBuffer.allocate(10);
        ByteBuffer readBuffer2 = ByteBuffer.allocate(5);
        ByteBuffer[] readBuffers = {readBuffer1, readBuffer2};
        datagramChannel.read(readBuffers);

        // 验证写入后的内容
        readBuffer1.flip();
        readBuffer2.flip();
        System.out.print(StandardCharsets.UTF_8.decode(readBuffer1));
        System.out.println();
        System.out.print(StandardCharsets.UTF_8.decode(readBuffer2));
        System.out.println();
        datagramChannel.close();
    }

    /**
     * Gather 聚集
     * 就是由多个Buffer组成一个Buffer数组，然后使用Channel的write方法依次读取数组各个Buffer中的数据写入通道（上一个Buffer读完之后再读下一个）
     */
    private static void gather() throws IOException {
        // 创建Channel
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(8000)).connect(new InetSocketAddress("127.0.0.1", 8000));

        // 读取各个Buffer内容写入Channel
        ByteBuffer writeBuffer1 = ByteBuffer.wrap("hello!".getBytes(StandardCharsets.UTF_8));
        ByteBuffer writeBuffer2 = ByteBuffer.wrap("world!".getBytes(StandardCharsets.UTF_8));
        ByteBuffer[] writeBuffers = {writeBuffer1, writeBuffer2};
        datagramChannel.write(writeBuffers);

        // 验证结果
        ByteBuffer readBuffer = ByteBuffer.allocate(12);
        datagramChannel.read(readBuffer);
        readBuffer.flip();
        System.out.println(StandardCharsets.UTF_8.decode(readBuffer));
        datagramChannel.close();

    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        scatter();
        gather();
    }

}
