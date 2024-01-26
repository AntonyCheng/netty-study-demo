package top.sharehome.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;

/**
 * 管道示例代码
 * Pipe主要用于线程之间的数据通信
 * *         ____________________________________
 * *         |              (Pipe)              |
 * *ThreadA--|-->Sink Channel-->Source Channel--|-->ThreadB
 * *         |__________________________________|
 *
 * @author AntonyCheng
 */
public class PipeDemo {

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        // 1、创建管道
        Pipe pipe = Pipe.open();

        // 2、获取Sink通道
        Pipe.SinkChannel sink = pipe.sink();

        // 3、创建缓冲区
        ByteBuffer sinkBuffer = ByteBuffer.wrap("hello world".getBytes(StandardCharsets.UTF_8));

        // 4、写入数据
        sink.write(sinkBuffer);
        // 5、关闭sinkChannel
        sink.close();

        new Thread(() -> {
            try {
                // 1、获取Source通道
                Pipe.SourceChannel source = pipe.source();
                // 2、创建缓冲区读取数据
                ByteBuffer sourceBuffer = ByteBuffer.allocate(1024);
                // 3、读取数据
                int read = source.read(sourceBuffer);
                System.out.println(new String(sourceBuffer.array(), 0, read));
                // 4、关闭sourceChannel
                source.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

}
