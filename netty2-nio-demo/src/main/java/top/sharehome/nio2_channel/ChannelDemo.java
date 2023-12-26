package top.sharehome.nio2_channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO三大核心——Channel通道
 * ---
 * NIO的通道类似于流，但是有如下区别：
 * 1、通道可以同时进行读写，而流只能读或者只能写，例如FileInputStream对象只能进行读取数据的操作。
 * 2、通道可以实现异步读写数据。
 * 3、通道可以从缓冲读数据，也可以写数据到缓冲。
 * ---
 * Channel在NIO中是一个接口，常用的Channel类如下：
 * 1、FileChannel ==> 用于文件的数据读写。
 * 2、DatagramChannel ==> 用于UDP的数据读写。
 * 3、ServerSocketChannel和SocketChannel ==> 用于TCP的数据读写。
 * ---
 * 接下来主要用FileChannel举例，其他的类以后会有对应的介绍。
 * FileChannel类常见的方法如下如下：
 * int read(ByteBuffer dst)	从通道读取数据并放到缓冲区中
 * int write(ByteBuffer src)	把缓冲区的数据写到通道中
 * long transferFrom(ReadableByteChannel src, long position, long count)	从目标通道中复制数据到当前通道
 * long transferTo(long position, long count, WritableByteChannel target)	把数据从当前通道复制给目标通道
 *
 * @author AntonyCheng
 */
public class ChannelDemo {

    public static void main(String[] args) throws IOException {
        byte[] bytes = "HelloWorld".getBytes();
        String filePath = System.getProperty("user.dir");
        String srcFilePath = filePath + "/netty2-nio-demo/src/main/java/top/sharehome/nio2_channel/file/1.txt";
        writeToFile(bytes, srcFilePath);
        readFromFile(srcFilePath);
        String destFilePath1 = filePath + "/netty2-nio-demo/src/main/java/top/sharehome/nio2_channel/file/2.txt";
        copyFileBySingleBuffer(srcFilePath, destFilePath1);
        String destFilePath2 = filePath + "/netty2-nio-demo/src/main/java/top/sharehome/nio2_channel/file/3.txt";
        transferFrom(srcFilePath, destFilePath2);
    }

    /**
     * 向本地文件写数据
     *
     * @param content  待写入数据内容
     * @param filePath 文件所在路径
     * @throws IOException
     */
    private static void writeToFile(byte[] content, String filePath) throws IOException {
        // 创建文件输入流
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        // 把流转为通道
        FileChannel channel = fileOutputStream.getChannel();
        // 将待写入的数据转换为ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(content);
        // 使用通道写入文件
        channel.write(buffer);
        // 关闭通道
        channel.close();
        // 关闭文件流
        fileOutputStream.close();
    }

    /**
     * 从本地文件读数据
     *
     * @param filePath 文件所在路径
     * @throws IOException
     */
    private static void readFromFile(String filePath) throws IOException {
        // 创建文件输出流
        FileInputStream fileInputStream = new FileInputStream(filePath);
        // 把流转为通道
        FileChannel channel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(fileInputStream.available());
        // 将通道数据读入到缓冲区
        channel.read(buffer);
        System.out.println(new String(buffer.array()));
        // 关闭通道
        channel.close();
        // 关闭文件流
        fileInputStream.close();
    }


    /**
     * 使用一个Buffer完成文件拷贝
     *
     * @param srcFilePath  源文件地址
     * @param destFilePath 目标文件地址
     * @throws IOException
     */
    private static void copyFileBySingleBuffer(String srcFilePath, String destFilePath) throws IOException {
        // 创建相关文件流
        FileInputStream srcFileStream = new FileInputStream(srcFilePath);
        FileOutputStream desFileStream = new FileOutputStream(destFilePath);
        // 把流转为通道
        FileChannel srcFileChannel = srcFileStream.getChannel();
        FileChannel desFileChannel = desFileStream.getChannel();
        // 申请缓存区内存
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 循环读取
        while (true) {
            // 一定要重置一下缓冲区的位置
            buffer.clear();
            int read = srcFileChannel.read(buffer);
            if (read == -1) {
                break;
            }
            // 转换由读模式转为写模式
            buffer.flip();
            desFileChannel.write(buffer);
        }
        // 关闭通道
        desFileChannel.close();
        srcFileChannel.close();
        // 关闭文件流
        desFileChannel.close();
        srcFileChannel.close();
    }

    /**
     * 使用Channel自带API进行文件拷贝
     *
     * @param srcFilePath  源文件地址
     * @param destFilePath 目标文件地址
     * @throws IOException
     */
    private static void transferFrom(String srcFilePath, String destFilePath) throws IOException {
        // 创建相关文件流
        FileInputStream srcFileStream = new FileInputStream(srcFilePath);
        FileOutputStream desFileStream = new FileOutputStream(destFilePath);
        // 把流转为缓冲区
        FileChannel srcFileChannel = srcFileStream.getChannel();
        FileChannel desFileChannel = desFileStream.getChannel();
        // 使用transferFrom方法完成拷贝
        desFileChannel.transferFrom(srcFileChannel, 0, srcFileChannel.size());
        // 关闭通道和流
        desFileChannel.close();
        srcFileChannel.close();
        desFileStream.close();
        srcFileStream.close();
    }
}