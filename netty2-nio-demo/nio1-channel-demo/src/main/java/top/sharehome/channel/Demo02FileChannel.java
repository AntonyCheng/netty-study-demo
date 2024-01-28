package top.sharehome.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel示例代码类
 * 主要用于处理文件的通道
 *
 * @author AntonyCheng
 */

public class Demo02FileChannel {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 1、创建FileChannel
     */
    public static void createFileChannel() throws FileNotFoundException {

        String path = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/1.txt";

        // 1、通过RandomAccessFile创建Channel通道（文件流可读可写）
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        FileChannel channel1 = randomAccessFile.getChannel();

        // 2、通过FileInputStream创建Channel通道（文件流可读）
        FileInputStream fileInputStream = new FileInputStream(path);
        FileChannel channel2 = fileInputStream.getChannel();

        // 3、通过FileOutputStream创建Channel通道（文件流可写）
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        FileChannel channel3 = fileOutputStream.getChannel();

    }

    /**
     * 2、通过FileChannel读数据到buffer中
     */
    public static void readByChannel() throws IOException {

        String path = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/1.txt";

        // 1、创建一个可读文件流Channel
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        FileChannel channel = randomAccessFile.getChannel();

        // 2、创建一个ByteBuffer，并将path放入
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 3、开始循环读取目标文件中的数据
        int readCount;
        //    使用Channel从读取文件到buffer中，对于Buffer而言此时就是写模式
        while ((readCount = channel.read(buffer)) != -1) {
            System.out.println("读取到了 " + readCount + " 个字节");
            // 从写模式转为读模式
            buffer.flip();
            // 判断buffer中是否还有没有读取的数据
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            // 清空buffer中的数据
            buffer.clear();
        }

        // 4、关闭通道和文件流
        channel.close();
        randomAccessFile.close();
        System.out.println("\n读取成功！");

    }

    /**
     * 3、通过FileChannel写数据到文件中
     */
    public static void writeByChannel() throws IOException {

        String path = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/1.txt";

        // 1、创建一个可写文件流Channel
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        FileChannel channel = randomAccessFile.getChannel();

        // 2、创建一个ByteBuffer，并将path放入
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(path.getBytes(StandardCharsets.UTF_8));
        //    将buffer模式从写改为读，才能让channel读取数据
        buffer.flip();
        // 3、使用Channel写入文件
        //    判断buffer中是否存在没有读取的数据
        if (buffer.hasRemaining()) {
            int writeCount = channel.write(buffer);
            System.out.println("成功写入 " + writeCount + " 个字节");
        }

        // 4、关闭通道和文件流
        channel.close();
        randomAccessFile.close();
        System.out.println("\n写入成功！");

    }

    /**
     * 4、读取文件内容写入另外一个文件中
     */
    public static void copy() throws IOException {

        String src = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/1.txt";
        String dest = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/1_tmp.txt";

        // 创建相应的读写Channel
        FileInputStream srcStream = new FileInputStream(src);
        FileChannel srcChannel = srcStream.getChannel();
        FileOutputStream destStream = new FileOutputStream(dest);
        FileChannel destChannel = destStream.getChannel();

        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(100);

        // 开始传输
        int count;
        while ((count = srcChannel.read(buffer)) != -1) {
            System.out.println("buffer读取到 " + count + " 个字节");
            // buffer写模式转读模式
            buffer.flip();
            while (buffer.hasRemaining()) {
                int writeCount = destChannel.write(buffer);
                System.out.println("从buffer写入 " + writeCount + " 个字节");
            }
            buffer.clear();
        }

        // 关闭通道和文件流
        srcChannel.close();
        destChannel.close();
        srcStream.close();
        destStream.close();

    }

    /**
     * 5、通道之间的数据传输
     * 也就是transferTo()和transferFrom()两个方法，这个也是文件复制效率最高的方法之一
     */
    private static void transfer() throws IOException {
        // 先创建两个通道
        String src = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/2.txt";
        String dest = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/2_tmp.txt";
        FileInputStream srcStream = new FileInputStream(src);
        FileChannel srcChannel = srcStream.getChannel();
        FileOutputStream destStream = new FileOutputStream(dest);
        FileChannel destChannel = destStream.getChannel();
        // 下面两行代码效果一样
        // 但要注意：
        // Linux系统中该方法能够传输任意大小的文件
        // Windows系统中该方法仅能够传输8M大小的文件，此时需要分段发送文件，可以提前计算一个文件需要发送的次数，然后再进行截取发送
        srcChannel.transferTo(0, srcChannel.size(), destChannel);
        destChannel.transferFrom(srcChannel, 0, srcChannel.size());
        // 关闭通道和文件流
        srcChannel.close();
        destChannel.close();
        srcStream.close();
        destStream.close();
    }


    /**
     * 6、其他常用API
     */
    private static void otherApis() throws IOException {

        // 1、position()方法：在通道绑定的文件某个位置进行读写操作
        // 例如现在有一个2.txt，内容是"hello world!"，要求只读取world!
        String path1 = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/3.txt";
        FileInputStream positionSrc = new FileInputStream(path1);
        FileChannel positionSrcChannel = positionSrc.getChannel().position(5);
        ByteBuffer bufferSrc = ByteBuffer.allocate(1);
        System.out.print("读取内容为：");
        while (positionSrcChannel.read(bufferSrc) != -1) {
            bufferSrc.flip();
            while (bufferSrc.hasRemaining()) {
                System.out.print((char) bufferSrc.get());
            }
            bufferSrc.clear();
        }
        System.out.println();
        positionSrcChannel.close();
        positionSrc.close();

        // 2、size()方法：获取通道绑定的文件大小
        // 例如现在有一个3.txt，内容是"123"，要求得到该文件的大小
        String path2 = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/4.txt";
        FileInputStream sizeSrc = new FileInputStream(path2);
        FileChannel sizeSrcChannel = sizeSrc.getChannel();
        long fileSize = sizeSrcChannel.size();
        System.out.println("文件大小为：" + fileSize);
        sizeSrcChannel.close();
        sizeSrc.close();

        // 3、truncate()方法，截取一个文件，
        // 例如现在有一个4.txt，内容是"112233445566"，要求变成"11223344"
        String path3 = PROJECT_PATH + "/netty2-nio-demo/nio1-channel-demo/src/main/java/top/sharehome/channel/file/5.txt";
        RandomAccessFile truncateSrc = new RandomAccessFile(path3, "rw");
        FileChannel truncateSrcChannel = truncateSrc.getChannel();
        // 截取前八个字符
        truncateSrcChannel.truncate(8);
        ByteBuffer truncateBuffer = ByteBuffer.allocate(10);
        System.out.print("截取后的内容为：");
        while (truncateSrcChannel.read(truncateBuffer) != -1) {
            truncateBuffer.flip();
            while (truncateBuffer.hasRemaining()) {
                System.out.print((char) truncateBuffer.get());
            }
            truncateBuffer.clear();
        }
        System.out.println();
        truncateSrcChannel.close();
        truncateSrc.close();

    }


    /**
     * 启动方法
     *
     * @param args 方法参数
     */
    public static void main(String[] args) throws Exception {
        otherApis();
    }

}
