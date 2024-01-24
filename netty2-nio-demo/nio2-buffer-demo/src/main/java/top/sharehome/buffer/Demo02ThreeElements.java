package top.sharehome.buffer;

import java.nio.ByteBuffer;

/**
 * Buffer三大要素
 * 1、capacity 缓冲区最大容量
 * 2、position 当前操作位置
 * 3、limit    当前操作限制的位置
 * 还有一个mark()空参方法，它负责记录三大要素的值。
 * position和limit的含义取决于Buffer处在读模式还是写模式。不管Buffer处在什么模式，capacity的含义总是一样的。
 * 1、capacity：
 * （1）作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”.你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。
 * 2、position：
 * （1）写数据到Buffer中时，position表示写入数据的当前位置，position的初始值为0。当一个byte、long等数据写到Buffer后，position会向下移动到下一个可插入数据的Buffer单元。position最大可为capacity–1（因为position的初始值为0）。
 * （2）读数据到Buffer中时，position表示读入数据的当前位置，如position=2时表示已开始读入了3个byte，或从第3个byte开始读取。通过ByteBuffer.flip()切换到读模式时position会被重置为0，当Buffer从position读入数据后，position会下移到下一个可读入的数据Buffer单元。
 * 3、limit：
 * （1）写数据时，limit表示可对Buffer最多写入多少个数据。写模式下，limit等于Buffer的capacity。
 * （2）读数据时，limit表示Buffer里有多少可读数据（notnull的数据），因此能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）。
 *
 *
 * @author AntonyCheng
 */

public class Demo02ThreeElements {

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 创建一个Buffer == 此时capacity=10、position=0、limit=10，即初始化了一个大小为10，从首部算起可写空间为10（limit-position）的缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(10);
        System.out.println(buffer.mark());

        // 向里面写入1个字符 == 此时capacity=10、position=1、limit=10
        buffer.put((byte) 'h');
        System.out.println(buffer.mark());
        // 再向里面写入4个字符 == 此时capacity=10、position=5、limit=10
        String ello = "ello";
        for (int i = 0; i < ello.length(); i++) {
            buffer.put((byte) ello.indexOf(0));
        }
        System.out.println(buffer.mark());

        // 写模式转为读模式 == 此时capacity=10、position=0、limit=5，即转为可读空间为5（limit-position）字符状态
        buffer.flip();
        System.out.println(buffer.mark());

        // 读两次 == 此时capacity=10、position=2、limit=5，即可读空间为3（limit-position）字符状态
        for (int i = 0; i < 2; i++) {
            buffer.get();
        }
        System.out.println(buffer.mark());

        // 清除已读数据转为写模式 == 此时capacity=10、position=3、limit=10，即将未读的3字符移动至缓冲区首部
        buffer.compact();
        System.out.println(buffer.mark());

        // 再向里面写入两个字符 == 此时capacity=10、position=5、limit=10
        for (int i = 0; i < 2; i++) {
            buffer.put((byte) '!');
        }
        System.out.println(buffer.mark());

        // 写模式转为读模式 == 此时capacity=10、position=0、limit=5
        buffer.flip();
        System.out.println(buffer.mark());

        // 全部读取 == 此时capacity=10、position=5、limit=5
        while (buffer.hasRemaining()){
            buffer.get();
        }
        System.out.println(buffer.mark());

        // 清除所有数据 == 此时capacity=10、position=0、limit=10
        buffer.clear();
        System.out.println(buffer.mark());
    }

}
