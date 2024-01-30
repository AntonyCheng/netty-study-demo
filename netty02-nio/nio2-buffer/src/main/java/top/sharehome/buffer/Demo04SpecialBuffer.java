package top.sharehome.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 特殊的缓冲区，也可以看作是对Buffer的一个进阶使用
 *
 * @author AntonyCheng
 */
public class Demo04SpecialBuffer {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 缓冲区分片
     * 在NIO中，除了可以分配或者包装一个缓冲区对象外，还可以根据现有的缓冲区对象来创建一个子缓冲区，即在现有缓冲区上切出一片来作为一个新的缓冲区，
     * 但现有的缓冲区与创建的子缓冲区在底层数组层面上是数据共享的，也就是说，子缓冲区相当于是现有缓冲区的一个视图窗口。
     * 调用slice()方法可以创建一个子缓冲区。
     */
    private static void slice() {
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.wrap("HelloWorld".getBytes());
        // 读取主缓冲区中的数据
        System.out.print("主缓冲区内容如下：");
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.print((char) buffer.get());
        }
        System.out.println();

        // 创建自定缓冲参数
        buffer.position(3);
        buffer.limit(7);

        // 创建子缓冲区
        ByteBuffer sliceBuffer = buffer.slice();

        // 读取子缓冲区的数据
        System.out.print("子缓冲区内容如下：");
        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            System.out.print((char) sliceBuffer.get());
        }
        System.out.println();

        // 从子缓冲区修改数据
        sliceBuffer.clear();
        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            sliceBuffer.put("!".getBytes());
        }

        // 主缓冲区内容
        buffer.clear();
        System.out.print("主缓冲区内容在修改后如下：");
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.print((char) buffer.get());
        }
        System.out.println();

        // 子缓冲区内容
        sliceBuffer.clear();
        System.out.print("主缓冲区内容在修改后如下：");
        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            System.out.print((char) sliceBuffer.get());
        }
        System.out.println();
    }

    /**
     * 只读缓冲区
     * 可以通过调用缓冲区的asReadOnlyBuffer()方法，将任何常规缓冲区转换为只读缓冲区，这个方法返回一个与原缓冲区完全相同的缓冲区，并与原缓冲区共享数据，只不过它是只读的。
     * 如果原缓冲区的内容发生了变化，只读缓冲区的内容也随之发生变化：
     */
    private static void readOnly() {
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.wrap("HelloWorld".getBytes());

        // 创建只读缓冲区
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        // 读取只读缓冲区中的内容
        System.out.print("只读缓冲区中的内容为：");
        while (readOnlyBuffer.hasRemaining()) {
            System.out.print((char) readOnlyBuffer.get());
        }
        readOnlyBuffer.rewind();
        System.out.println();

        // 修改原缓冲区中的数据
        buffer.clear();
        buffer.position(5);
        for (int i = 0; i < buffer.capacity() - 5; i++) {
            buffer.put("!".getBytes());
        }

        // 再次读取只读缓冲区中的内容
        System.out.print("修改后只读缓冲区中的内容为：");
        while (readOnlyBuffer.hasRemaining()) {
            System.out.print((char) readOnlyBuffer.get());
        }
        System.out.println();
    }

    /**
     * 直接缓冲区
     * allocateDirect（直接分配）：
     * 1、使用allocateDirect方法分配的内存是直接在操作系统的堆外内存（off-heap）中分配的，而不是在Java虚拟机的堆内存中。
     * 2、这种方式的内存分配通常更加高效，因为它直接利用了操作系统的内存管理机制，减少了Java堆内存管理的开销。
     * 3、由于在堆外内存中分配，这些对象不受Java垃圾回收的管理。
     * allocate（堆分配）：
     * 1、使用allocate方法分配的内存是在Java虚拟机的堆内存中分配的。
     * 2、这种方式的内存分配相对较慢，因为它涉及到Java垃圾回收的管理和堆内存的分配。
     * 3、由于在堆内内存中分配，这些对象受Java垃圾回收的管理。
     */
    private static void direct() {
        // 创建一个直接内存缓冲区
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(10);
    }

    /**
     * 内存映射文件I/O
     * 内存映射文件I/O是一种读和写文件数据的方法，它可以比常规的基于流或者基于通道的I/O快的多。
     * 内存映射文件I/O是通过使文件中的数据出现为内存数组的内容来完成的，这其初听起来似乎不过就是将整个文件读到内存中，但是事实上并不是这样。
     * 一般来说，只有文件中实际读取或者写入的部分才会映射到内存中，当然这样的编码难度会大大增加。
     */
    private static void mappingIo() throws IOException {
        // 创建文件，内容是"HelloWorld"
        String path = PROJECT_PATH + "/netty2-nio-demo/nio2-buffer/src/main/java/top/sharehome/buffer/file/2.txt";
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        // 创建Channel通道
        FileChannel channel = randomAccessFile.getChannel();

        // 由Channel通道创建MappedByteBuffer
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, randomAccessFile.length());

        // 接下来将内容改变成为"hello  old123"
        mappedByteBuffer.put(0, (byte) 'h');
        mappedByteBuffer.put(5, (byte) ' ');
        mappedByteBuffer.put(6, (byte) ' ');
        mappedByteBuffer.put(7, (byte) 'o');

        // 关闭通道和文件流
        channel.close();
        randomAccessFile.close();
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        mappingIo();
    }

}
