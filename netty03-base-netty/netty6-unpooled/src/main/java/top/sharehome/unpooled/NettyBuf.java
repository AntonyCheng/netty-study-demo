package top.sharehome.unpooled;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Netty 缓冲区（会和NIO进行对照）
 * 首先注意一点就是在 Netty 缓冲区中没有“读写转换”的操作。
 *
 * @author AntonyCheng
 */

public class NettyBuf {

    /**
     * 1、创建操作
     */
    public static void create() {
        // 1-1 NIO
        ByteBuffer nioBuffer1 = ByteBuffer.allocate(10);
        ByteBuffer nioBuffer2 = ByteBuffer.allocateDirect(10);
        ByteBuffer nioBuffer3 = ByteBuffer.wrap("HelloWorld".getBytes(StandardCharsets.UTF_8));

        // 1-2 Netty
        ByteBuf nettyBuffer1 = Unpooled.buffer(10);
        ByteBuf nettyBuffer2 = Unpooled.directBuffer(10);
        ByteBuf nettyBuffer3 = Unpooled.copiedBuffer("HelloWorld", StandardCharsets.UTF_8);
    }

    /**
     * 2、读操作
     */
    public static void read() {
        // 2-1 NIO
        System.out.println("NIO read:");
        ByteBuffer nioBuffer = ByteBuffer.wrap("HelloWorld".getBytes(StandardCharsets.UTF_8));
        // 读取NIO buffer中的所有数据
        for (int i = 0; i < nioBuffer.capacity(); i++) {
            System.out.print((char) nioBuffer.get());
        }
        System.out.println();

        // 2-2 Netty
        System.out.println("Netty read:");
        ByteBuf nettyBuffer = Unpooled.copiedBuffer("HelloWorld", StandardCharsets.UTF_8);
        // 先获取到可读个数，因为缓冲区数值是变化的，每次循环都会发生改变，循环变量也就会跟着发生改变
        int length = nettyBuffer.writerIndex() - nettyBuffer.readerIndex();
        // int length = nettyBuffer.readableBytes();
        for (int i = 0; i < length; i++) {
            System.out.print((char) nettyBuffer.readByte());
        }
    }

    /**
     * 3、写操作
     */
    public static void write() {
        // 3-1 NIO
        ByteBuffer nioBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < nioBuffer.capacity(); i++) {
            nioBuffer.put((byte) ((char) i));
        }
        System.out.println(Arrays.toString(nioBuffer.array()));

        // 3-2 Netty
        ByteBuf nettyBuffer = Unpooled.buffer(10);
        // 获取capacity，由于它是动态的，每次循环都会随着写入数量的增大而增大，进而造成死循环，同时会造成缓冲区无限扩容。
        int capacity = nettyBuffer.capacity();
        for (int i = 0; i < capacity; i++) {
            nettyBuffer.writeByte((byte) ((char) i));
        }
        System.out.println(Arrays.toString(nioBuffer.array()));
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 下面进行一个ByteBuf写入读取的完整示例
        ByteBuf buffer = Unpooled.buffer(10);
        // 写入
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.writeByte(i);
        }
        // 读取
        int length1 = buffer.readableBytes();
        for (int i = 0; i < length1; i++) {
            System.out.println(buffer.readByte());
        }
        // 清除，如果没有清除，继续接着写入，buffer会扩容
        //buffer.clear();
        System.out.println();
        // 写入
        int capacity = buffer.capacity();
        for (int i = 0; i < capacity; i++) {
            buffer.writeByte(i + 1);
        }
        // 读取
        int length2 = buffer.readableBytes();
        for (int i = 0; i < length2; i++) {
            System.out.println(buffer.readByte());
        }
        buffer.clear();
    }

}
