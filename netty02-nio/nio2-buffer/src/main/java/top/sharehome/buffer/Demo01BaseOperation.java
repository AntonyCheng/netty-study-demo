package top.sharehome.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Buffer基础操作
 * 基础操作一般遵循以下五个步骤：
 * 1、创建Buffer
 * 2、写入数据到Buffer
 * 3、调用flip()方法让其写模式转读模式
 * 4、从Buffer将数据读出
 * 5、调用clear()或compact()方法清除Buffer内容并转为写模式
 * 注意：
 * 1、大多数创建方式在创建后默认写模式，并不是全部；
 * 2、clear()方法清除的是全部数据，compact方法清除的是已读数据。
 *
 * @author AntonyCheng
 */
public class Demo01BaseOperation {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 1、创建操作
     */
    public static void create() {
        // 1、通过allocate()方法从堆区创建大小为1024的写模式Buffer
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        // 2、通过allocateDirect()方法从内存创建大小为1024的写模式Buffer
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
        // 3、通过wrap()方法从堆区创建大小为参数字节数组大小的读模式Buffer
        ByteBuffer wrap = ByteBuffer.wrap("some bytes".getBytes());
        // 4、通过charset字符集从堆区创建大小为参数字节数组大小的读模式Buffer，下面两种写法效果一样，但推荐第一种
        ByteBuffer charset1 = StandardCharsets.UTF_8.encode("some bytes");
        ByteBuffer charset2 = StandardCharsets.UTF_8.encode("some bytes");
        //    第四种主要用于对于数据字符集有特殊要求的内容，相当于对内容进行编码，如果需要解码需要使用decode()方法
        CharBuffer decode1 = StandardCharsets.UTF_8.decode(charset1);
        CharBuffer decode2 = StandardCharsets.UTF_8.decode(charset2);
    }

    /**
     * 2、读操作
     * （1）方法一：使用Channel.write()方法将Buffer中的数据读取到Channel里（Channel实例中有演示）
     * （2）方法二：使用get()方法进行直接读取
     */
    private static void read() throws IOException {
        // 创建通道
        RandomAccessFile randomAccessFile = new RandomAccessFile(PROJECT_PATH + "/netty2-nio-demo/nio2-buffer/src/main/java/top/sharehome/buffer/file/1.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(5);

        // 循环读取Channel中的内容到Buffer中
        int count;
        while ((count = channel.read(buffer)) != -1) {
            System.out.println("读取到了 " + count + " 个字符");
            // 从读模式切换到写模式
            buffer.flip();
            // 监控buffer中是否还有没有操作的元素
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            // 清空buffer中的数据
            buffer.clear();
        }

        // 关闭通道和文件流
        channel.close();
        randomAccessFile.close();
    }

    /**
     * 3、写操作
     * （1）方法一：使用Channel.read()方法将Channel中的数据写入到Buffer里（Channel实例中有演示）
     * （2）方法二：使用put()方法进行直接写入
     */
    private static void write() throws IOException {
        // 创建通道
        RandomAccessFile randomAccessFile = new RandomAccessFile(PROJECT_PATH + "/netty2-nio-demo/nio2-buffer/src/main/java/top/sharehome/buffer/file/1.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();

        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 将数据写入buffer
        buffer.put((byte) 'b');
        buffer.put((byte) 'u');
        buffer.put((byte) 'f');
        buffer.put((byte) 'f');
        buffer.put((byte) 'e');
        buffer.put((byte) 'r');

        // 将Buffer从写模式转为读模式
        buffer.flip();

        // 通过channel读取buffer中的数据写入文件
        while (buffer.hasRemaining()) {
            int count = channel.write(buffer);
            System.out.println("写入了 " + count + " 个字符");
        }

        // 关闭通道和文件流
        channel.close();
        randomAccessFile.close();
    }

    /**
     * XxxBuffer示例
     */
    private static void otherBuffer() {
        // 创建一个IntBuffer
        IntBuffer intBuffer = IntBuffer.allocate(1024);

        // 循环插入数据
        for (int i = 0; i < 10; i++) {
            intBuffer.put(i);
        }

        // 将IntBuffer从写模式转换为读模式
        intBuffer.flip();

        // 循环读取数据
        for (int i = 0; i < 10; i++) {
            System.out.println("value" + (i + 1) + ": " + intBuffer.get());
        }
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        otherBuffer();
    }

}
