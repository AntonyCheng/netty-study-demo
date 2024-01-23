package top.sharehome.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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
        ByteBuffer charset2 = Charset.forName("UTF-8").encode("some bytes");
        //    第四种主要用于对于数据字符集有特殊要求的内容，相当于对内容进行编码，如果需要解码需要使用decode()方法
        CharBuffer decode1 = StandardCharsets.UTF_8.decode(charset1);
        CharBuffer decode2 = Charset.forName("UTF-8").decode(charset2);
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) {

    }

}
