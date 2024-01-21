package top.sharehome.channel;


import java.nio.ByteBuffer;

/**
 * 初次尝试buffer示例代码
 * 这里主要是为了使用Channel做铺垫，所以在这里只需要了解部分操作即可：
 * 1、创建Buffer缓冲区，allocate()创建默认写模式
 * 注意：NIO buffer有读写两种模式，原则上写模式下不能读，反之依然，但是实际上NIO允许写模式下调用读方法，只不过读的内容不一定是功能所需的，但是写模式下调用读方法会报错；
 * 2、put()写入数据
 * 3、flip()读写模式转换（写模式转为读模式）
 * 4、get()读取数据
 * 5、clear()或者compact()清除数据，均自动从读模式转换为写模式
 * 6、......（以上操作循环往复）
 *
 * @author AntonyCheng
 */

public class Demo01TryBuffer {

    public static void main(String[] args) {
        // 创建大小为12的缓冲区，这种方式默认写模式
        ByteBuffer allocate = ByteBuffer.allocate(12);
        System.out.println("init buffer:" + new String(allocate.array()) + "\t" + allocate.mark());
        // 写入12个数据
        allocate.put("HelloWorld!!".getBytes());
        System.out.println("after write:" + new String(allocate.array()) + "\t" + allocate.mark());
        // 写转读
        allocate.flip();
        System.out.println("write to read:" + new String(allocate.array()) + "\t" + allocate.mark());
        // 读取10个数据
        for (int i = 0; i < 10; i++) {
            System.out.print((char) allocate.get());
        }
        System.out.println("\nafter read:" + new String(allocate.array()) + "\t" + allocate.mark());
        // “清除”缓冲区内容，且自动转为写模式，但如果还存在未读数据，将未读数据压缩到缓冲区头部，下一次就从未读数据之后开始写入
        allocate.compact();
        System.out.println("after compact and read to write:" + new String(allocate.array()) + "\t" + allocate.mark());
        // 再次写入10个数据
        allocate.put("HELLOWORLD".getBytes());
        System.out.println("after write again:" + new String(allocate.array()) + "\t" + allocate.mark());
        // 写转读
        allocate.flip();
        System.out.println("write to read:" + new String(allocate.array()) + "\t" + allocate.mark());
        // 读取12个数据
        for (int i = 0; i < 12; i++) {
            System.out.print((char) allocate.get());
        }
        System.out.println("\nafter read again:" + new String(allocate.array()) + "\t" + allocate.mark());
        // “清除”缓冲区内容，且自动转为写模式，忽略未读数据，下一次就从缓冲区头部开始写入
        allocate.clear();
        System.out.println("after clear and read to write:" + new String(allocate.array()) + "\t" + allocate.mark());
    }

}