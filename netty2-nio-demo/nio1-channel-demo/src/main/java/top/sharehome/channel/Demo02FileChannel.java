package top.sharehome.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel示例代码类
 *
 * @author AntonyCheng
 */

public class Demo02FileChannel {

    // 使用FileChannel读取数据到ByteBuffer中
    public static void main(String[] args) throws Exception {
        // 创建FileChannel
        FileInputStream fileInputStream = new FileInputStream("D:\\SSMLearning\\project-demo\\netty-study-demo\\netty2-nio-demo\\nio1-channel-demo\\src\\main\\java\\top\\sharehome\\channel\\file\\1.txt");
        FileChannel channel = fileInputStream.getChannel();

        // 创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取数据到Buffer中
        int read;
        while ((read = channel.read(buffer)) != -1) {
            System.out.println("读取到了 " + read + " 个字符！");
            // 写转读
            buffer.flip();
            // 判断buffer还有没有数据可读
            while (buffer.hasRemaining()) {
                System.out.print((char)buffer.get());
            }
            // 读取完成之后清空buffer
            buffer.clear();
        }
        fileInputStream.close();
        System.out.println("\n读取成功！");
    }

}
