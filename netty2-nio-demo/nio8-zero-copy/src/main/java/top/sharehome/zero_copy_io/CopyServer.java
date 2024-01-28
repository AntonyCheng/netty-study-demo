package top.sharehome.zero_copy_io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 零拷贝服务端
 *
 * @author AntonyCheng
 */

public class CopyServer {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9999));

        System.out.println("文件服务器启动成功...");

        ByteBuffer buffer = ByteBuffer.allocate(4096);

        RandomAccessFile randomAccessFile = new RandomAccessFile(PROJECT_PATH+"/netty2-nio-demo/nio8-zero-copy/src/main/java/top/sharehome/zero_copy_io/file/2.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        for (; ; ) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            while (socketChannel.read(buffer) != -1) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
        }
    }

}
