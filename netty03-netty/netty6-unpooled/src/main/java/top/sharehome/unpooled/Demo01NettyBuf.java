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

public class Demo01NettyBuf {

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
        // 读取Netty buffer中的可读数据
//        for (int i = 0; i < nettyBuffer.writerIndex(); i++) {
//            System.out.print((char) nettyBuffer.readByte());
//        }
        for (int i = 0; i < nettyBuffer.readableBytes(); i++) {
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
            nioBuffer.put((byte)((char)i));
        }
        System.out.println(Arrays.toString(nioBuffer.array()));

        // 3-2 Netty
        ByteBuf nettyBuffer = Unpooled.buffer(10);
        for (int i = 0; i < nettyBuffer.capacity(); i++) {
            nettyBuffer.writeByte((byte)((char)i));
        }
        System.out.println(Arrays.toString(nioBuffer.array()));
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        read();
    }

}
