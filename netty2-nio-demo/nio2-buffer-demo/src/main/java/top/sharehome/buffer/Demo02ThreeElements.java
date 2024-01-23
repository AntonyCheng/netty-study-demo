package top.sharehome.buffer;

/**
 * todo：补充示例代码
 * Buffer三大要素
 * 1、capacity 缓冲区最大容量
 * 2、position 当前操作位置
 * 3、limit    当前操作限制的位置
 * position和limit的含义取决于Buffer处在读模式还是写模式。不管Buffer处在什么模式，capacity的含义总是一样的。
 * 1、capacity：
 * （1）作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”.你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。
 * 2、position：
 * （1）写数据到Buffer中时，position表示写入数据的当前位置，position的初始值为0。当一个byte、long等数据写到Buffer后，position会向下移动到下一个可插入数据的Buffer单元。position最大可为capacity–1（因为position的初始值为0）。
 * （2）读数据到Buffer中时，position表示读入数据的当前位置，如position=2时表示已开始读入了3个byte，或从第3个byte开始读取。通过ByteBuffer.flip()切换到读模式时position会被重置为0，当Buffer从position读入数据后，position会下移到下一个可读入的数据Buffer单元。
 * 3、limit：
 * （1）写数据时，limit表示可对Buffer最多写入多少个数据。写模式下，limit等于Buffer的capacity。
 * （2）读数据时，limit表示Buffer里有多少可读数据（notnull的数据），因此能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）。
 * @author AntonyCheng
 */

public class Demo02ThreeElements {
}
