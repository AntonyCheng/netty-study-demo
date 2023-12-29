package top.sharehome.nio3_buffer2;

import java.nio.ByteBuffer;

/**
 * 这个BufferDemo是对Buffer进行一层拔高，这里将演示一些Buffer的特性或者是结合Channel更高级的用法
 *
 * @author AntonyCheng
 */
public class BufferDemo {

    public static void main(String[] args) {
        bufferApiMarkAndReset();
    }

    /**
     * Flip和Rewind示例代码和解释
     */
    public static void bufferApiFlipAndRewind() {
        // 创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        byte[] bytes = "HelloWorld".getBytes();
        // 填充Buffer
        buffer.put(bytes);
        System.out.println(getContent(1, buffer));
        // 写转读 [limit == position, position == 0, mark == -1 (重置mark为无效)]
        buffer.flip();
        System.out.println(getContent(2, buffer));
        for (int i = 0; i < buffer.limit() / 2; i++) {
            System.out.println((char) buffer.get());
        }
        System.out.println(getContent(3, buffer));
        // todo
    }

    public static void bufferApiClearAndCompact() {

    }

    /**
     * Mark和Reset示例代码和解释
     * Mark方法就是设置一个Buffer标志状态，Reset方法就是回到Mark方法设置的Buffer标志状态
     * 这两个方法在同一模式（读写模式）下始终是配合使用的，即同一模式下先有Mark再有Reset
     * 不能是先有Reset再有Mark，因为在Mark之前mark值一直是-1（无效值）
     * 不能是写模式下的Mark用读模式下的Reset，因为一旦读写转换，mark值就会重置为-1
     */
    public static void bufferApiMarkAndReset() {
        // 创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        byte[] bytes1 = "Hel".getBytes();
        byte[] bytes2 = "HelloWo".getBytes();
        byte[] bytes3 = "HelloWorld".getBytes();
        // 写之前设置一个标志位
        buffer.mark();
        // 将bytes1写入Buffer
        buffer.put(bytes1);
        System.out.println(getContent(1, buffer));
        // 写完之后重置标志位
        buffer.reset();
        // 将byte2写入Buffer
        buffer.put(bytes2);
        System.out.println(getContent(2, buffer));
        // 写完之后重置标志位
        buffer.reset();
        // 将byte3写入Buffer
        buffer.put(bytes3);
        System.out.println(getContent(3, buffer));
        // 写转读
        buffer.flip();
        // 再次使用Mark方法打一个标志
        buffer.mark();
        System.out.println(getContent(4, buffer));
        System.out.println((char) buffer.get());
        // 读一个之后重置标志位
        buffer.reset();
        System.out.println(getContent(5, buffer));
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        // 读两个之后重置标志位
        buffer.reset();
        System.out.println(getContent(6, buffer));
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println(getContent(7, buffer));
    }

    private static String getContent(ByteBuffer buffer) {
        return "内容为：" + new String(buffer.array()) + " capacity为：" + buffer.capacity() + " position为：" + buffer.position() + " limit为：" + buffer.limit();
    }

    private static String getContent(int sign, ByteBuffer buffer) {
        return sign + "==>" + "内容为：" + new String(buffer.array()) + " capacity为：" + buffer.capacity() + " position为：" + buffer.position() + " limit为：" + buffer.limit();
    }

}
