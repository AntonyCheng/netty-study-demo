package top.sharehome.zero_copy_io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * 零拷贝客户端
 *
 * @author AntonyCheng
 */

public class CopyClient {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));

        RandomAccessFile randomAccessFile = new RandomAccessFile(PROJECT_PATH + "/netty2-nio-demo/nio8-zero-copy/src/main/java/top/sharehome/zero_copy_io/file/1.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        long size = fileChannel.size();
        // 零拷贝
        fileChannel.transferTo(0, size, socketChannel);

        fileChannel.close();
        randomAccessFile.close();
        socketChannel.close();
    }

}
