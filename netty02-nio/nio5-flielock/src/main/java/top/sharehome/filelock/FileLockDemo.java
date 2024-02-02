package top.sharehome.filelock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件锁示例代码
 *
 * @author AntonyCheng
 */
public class FileLockDemo {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 获取文件锁的方法
     * 1、lock()：获取阻塞排它锁
     * 2、tryLock()：获取非阻塞排它锁
     * 3、lock(position,start,True)：获取阻塞共享锁
     * 4、tryLock(position,start,True)：获取非阻塞共享锁
     * 注意：一个文件只能被获取一次文件锁，所以上面的方法不能在某个文件被锁状态下被对该文件重复使用
     */
    private static void getFileLock() throws IOException {
        // 创建FileChannel
        String path = PROJECT_PATH + "/netty2-nio-demo/nio5-flielock/src/main/java/top/sharehome/filelock/file/1.txt";
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "rw");
        FileChannel channel = randomAccessFile.getChannel();

        // 获取阻塞排它锁
        FileLock lock1 = channel.lock();
        // 获取非阻塞排它锁
        //FileLock lock2 = channel.tryLock();
        // 获取阻塞共享锁
        //FileLock lock3 = channel.lock(0, channel.size(), true);
        // 获取非阻塞共享锁
        //FileLock lock4 = channel.tryLock(0, channel.size(), true);
    }

    /**
     * 方法入口
     * 先对文件进行追加，然后再进行全部内容的读取
     */
    public static void main(String[] args) throws IOException {
        // 创建文件Path
        String path = PROJECT_PATH + "/netty2-nio-demo/nio5-flielock/src/main/java/top/sharehome/filelock/file/1.txt";
        Path filePath = Paths.get(path);
        // 创建一个追加写的FileChannel
        FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        // 创建写buffer
        ByteBuffer writeBuffer = ByteBuffer.wrap("Hello world".getBytes(StandardCharsets.UTF_8));

        // 获取锁，进而加锁操作
        FileLock lock = fileChannel.lock();
        System.out.println("是否是一个共享锁：" + lock.isShared());
        // 进行写操作
        fileChannel.write(writeBuffer);
        fileChannel.close();

        // 接下来进行读操作
        FileReader fileReader = new FileReader(PROJECT_PATH + "/netty2-nio-demo/nio5-flielock/src/main/java/top/sharehome/filelock/file/1.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        bufferedReader.close();
        fileReader.close();
    }

}
