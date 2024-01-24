package top.sharehome.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 一些其他的API
 *
 * @author AntonyCheng
 */
public class Demo03OtherApis {

    /**
     * position()方法&capacity()方法&limit()方法
     * 获取三大要素
     */
    private static void getThreeElements() {
        // 创建缓冲区 == 此时capacity=10、position=0、limit=10
        ByteBuffer buffer = ByteBuffer.wrap("HelloWorld".getBytes());

        int position = buffer.position();
        int limit = buffer.limit();
        int capacity = buffer.capacity();

        System.out.println(buffer.mark());

        // 其中position和limit方法还能自定义参数
        // 此时capacity=10、position=5、limit=10
        buffer.position(5);
        System.out.println(buffer.mark());
        // 此时capacity=10、position=5、limit=6
        buffer.limit(6);
        System.out.println(buffer.mark());
    }

    /**
     * rewind()方法
     * 重读方法，在读模式下使用，它可以将position置为0，是缓冲区能够被重新读取
     */
    private static void rewind() {
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.wrap("HelloWorld".getBytes());

        // 打印十次buffer中的内容
        for (int i = 0; i < 10; i++) {
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            System.out.println();
            buffer.rewind();
        }
    }

    /**
     * mark()方法&reset()方法
     * 类似于Linux中的快照或者数据库中的事务，使用mark()之后能够将Buffer最近的一次状态进行保存，在同一模式下调用reset()就能回到这个状态
     * mark()方法就是设置一个Buffer标志状态，reset()方法就是回到mark()方法设置的Buffer标志状态
     * 这两个方法在同一模式（读写模式）下始终是配合使用的，即同一模式下先有mark()再有reset()
     * 不能是先有reset()再有mark()，因为在mark()之前mark值一直是-1（无效值）
     * 不能是写模式下的mark()用读模式下的reset()，因为一旦读写转换，mark值就会重置为-1
     */
    private static void snapshot() {
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.wrap("HelloWorld".getBytes());

        // 先读取两个元素
        for (int i = 0; i < 2; i++) {
            buffer.get();
        }
        System.out.println("position: " + buffer.position() + "\tlimit: " + buffer.limit() + "\tcapacity: " + buffer.capacity());
        // 保存快照
        buffer.mark();
        // 再读取3个元素
        for (int i = 0; i < 3; i++) {
            buffer.get();
        }
        System.out.println("position: " + buffer.position() + "\tlimit: " + buffer.limit() + "\tcapacity: " + buffer.capacity());
        // 回复快照
        buffer.reset();
        System.out.println("position: " + buffer.position() + "\tlimit: " + buffer.limit() + "\tcapacity: " + buffer.capacity());
    }

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        snapshot();
    }

}
