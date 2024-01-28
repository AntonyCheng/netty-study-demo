package top.sharehome.otherapis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * 异步FileChannel，就是异步去操作文件
 *
 * @author AntonyCheng
 */
public class Demo03AsynchronousFileChannel {

    private static final String PATH = System.getProperty("user.dir") + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/file/1.txt";

    /**
     * 创建异步FileChannel
     * 结合Path类API进行创建
     */
    private static void create() throws IOException {
        Path path = Paths.get(PATH);
        AsynchronousFileChannel asynchronousFileReadChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        AsynchronousFileChannel asynchronousFileWriteChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
    }

    /**
     * 通过异步FileChannel对文件进行读取（通过Future类进行读取）
     */
    private static void readByFuture() throws IOException {
        // 1、创建异步FileChannel
        Path path = Paths.get(PATH);
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

        // 2、创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 3、调用Channel中的read方法获得Future对象
        Future<Integer> future = asynchronousFileChannel.read(buffer, 0);

        // 4、判断是否完成isDone()，返回true则表示传输完成
        while (!future.isDone()) {
            System.out.println("正在读取...");
        }

        // 5、打印读取完成之后的数据
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        buffer.clear();

        // 6、读取完成之后关闭通道
        asynchronousFileChannel.close();
    }

    /**
     * 通过异步FileChannel对文件进行读取（通过实现CompletionHandler接口进行读取）
     */
    private static void readByCompletionHandler() throws IOException {
        // 1、创建异步FileChannel
        Path path = Paths.get(PATH);
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

        // 2、创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 3、调用Channel中的read方法，实现其中的接口参数（其中第三个参数是attachment，这个值可以被带入到异步操作completed和failed中，所以允许为null，表示不需要携带额外数据进入异步操作）
        asynchronousFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            // 4、当读取完成时进行的操作
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("读取了 " + result + " 个字节");
                attachment.flip();
                while (attachment.hasRemaining()) {
                    System.out.print((char) attachment.get());
                }
                attachment.clear();
            }

            // 5、当出现异常时进行的操作
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                throw new RuntimeException(exc);
            }
        });

        // 6、读取完成之后关闭通道
        asynchronousFileChannel.close();
    }

    /**
     * 通过异步FileChannel对文件进行写入（通过Future类进行写入）
     */
    private static void writeByFuture() throws IOException {
        // 1、创建异步FileChannel
        Path path = Paths.get(PATH);
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

        // 2、创建Buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));

        // 3、调用Channel中的read方法获得Future对象
        Future<Integer> future = asynchronousFileChannel.write(buffer, 0);

        // 4、判断是否完成isDone()，返回true则表示传输完成
        while (!future.isDone()) {
            System.out.println("正在写入...");
        }

        // 5、写入完成之后关闭通道
        asynchronousFileChannel.close();

        // 6、读取写入完成之后的文件数据
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        // 7、关闭Reader
        bufferedReader.close();
    }

    /**
     * 通过异步FileChannel对文件进行写入（通过实现CompletionHandler接口进行写入）
     */
    private static void writeByCompletionHandler() throws IOException {
        // 1、创建异步FileChannel
        Path path = Paths.get(PATH);
        AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

        // 2、创建Buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));

        // 3、调用Channel中的write方法，实现其中的接口参数（其中第三个参数是attachment，这个值可以被带入到异步操作completed和failed中，所以允许为null，表示不需要携带额外数据进入异步操作）
        asynchronousFileChannel.write(buffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
            // 4、当写入完成时进行的操作
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("写入了 " + result + " 个字节");
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new FileReader(PATH));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // 5、当出现异常时进行的操作
            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                throw new RuntimeException(exc);
            }
        });

        // 6、写入完成之后关闭通道
        asynchronousFileChannel.close();
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        writeByCompletionHandler();

        // 因为异步，所以需要阻塞主进程
        System.in.read();
    }

}
