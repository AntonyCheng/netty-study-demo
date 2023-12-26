package top.sharehome.nio1_buffer;

import java.nio.ByteBuffer;

/**
 * NIO三大核心——Buffer缓冲区
 * ---
 * 缓冲区本质上是一个可以读写数据的内存块，可以理解成是一个容器对象（含数组），该对象提供了一组方法，可以更轻松地使用内存块，缓冲区对象内置了一些机制，能够跟踪和记录缓冲区的状态变化情况。Channel提供从文件、网络读取数据的渠道，但是读取或写入的数据必须经由Buffer。
 * 在Java中Buffer是一个抽象类，它将不同的内存申请模式和除boolean出外的基本类型相结合，封装了非常多的子类，但是用的最多的还是ByteBuffer类。
 * Buffer 类定义了所有的缓冲区都具有的四个属性来提供关于其所包含的数据元素的信息。
 * 1、capacity：容量，既可以容纳的最大数据量，在缓冲区申请时被设定且不能改变。
 * 2、limit：表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作，且极限是可以修改的。
 * 3、position：当前位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变该值，为下次读写做准备，默认为0。
 * 4、mark：位置标记，默认为-1。
 * ---
 * Buffer抽象类常用方法如下：
 * int capacity()	返回此缓冲区的容量
 * int position()	返回此缓冲区的位置
 * Buffer position(int newPosition)	设置此缓冲区的位置
 * int limit()	返回此缓冲区的限制
 * Buffer limit(int newLimit)	设置此缓冲区的限制
 * Buffer mark()	在此缓冲区的位置设置标记
 * Buffer reset()	将此缓冲区的位置重置为以前标记的位置
 * Buffer clear()	清除此缓冲区，即将各个标记恢复到初始状态，但是数据并没有真正擦除
 * Buffer flip()	反转此缓冲区
 * Buffer rewind()	重绕此缓冲区
 * int remaining()	返回当前位置与限制之间的元素数
 * boolean hasRemaining()	告知在当前位置和限制之间是否有元素
 * boolean isReadOnly()	告知此缓冲区是否为只读缓冲区
 * boolean hasArray()	告知此缓冲区是否具有可访问的底层实现数组
 * Object array()	返回此缓冲区的底层实现数组
 * int arrayOffset()	返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
 * boolean isDirect()	告知此缓冲区是否为直接缓冲区
 * ---
 * 接下来的一些示例将以ByteBuffer为例编写代码。
 * 该类除了以上的父类方法，还有以下常用方法：
 * ByteBuffer allocateDirect(int capacity)	创建直接缓冲区（从内存直接），创建后默认写模式；
 * ByteBuffer allocate(int capacity)	设置缓冲区的初始容量（从堆区创建），创建后默认写模式
 * ByteBuffer wrap(byte[] array)	把一个数组放到缓冲区中使用，创建后默认读模式
 * ByteBuffer wrap(byte[] array,int offset, int length)	构造初始化位置offset和上界length的缓冲区，创建后默认读模式
 * byte get()	从当前位置position上get，get之后，position会自动+1
 * byte get(int index)	从绝对位置get
 * ByteBuffer put(byte b)	从当前位置上put，put之后，position会自动+1
 * ByteBuffer put(int index, byte b)	从绝对位置上put
 *
 * @author AntonyCheng
 */
public class BufferDemo {

    public static void main(String[] args) {
        // 从堆区创建一个ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println(getContent(buffer));
        // 创建一个测试byte数组
        byte[] bytes = "HelloWorld".getBytes();
        // 向缓冲区中增加元素
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put(bytes[i]);
        }
        System.out.println(getContent(buffer));
        // 将缓冲区从写模式转换成读模式
        buffer.flip();
        // 从缓冲区中读数据（读一半）
        for (int i = 0; i < buffer.limit() / 2; i++) {
            System.out.println((char) buffer.get());
        }
        System.out.println(getContent(buffer));
        // 将缓冲区从读模式转换成写模式
        buffer.flip();
        System.out.println(getContent(buffer));
        // 创建一个临时数组
        byte[] temp = "World".getBytes();
        // 将临时数组写入缓冲区
        for (byte b : temp) {
            buffer.put(b);
        }
        System.out.println(getContent(buffer));
        // 将缓冲区从写模式转换成读模式
        buffer.flip();
        System.out.println(getContent(buffer));
        // 清除缓冲区中的所有标记，重置为最原始的模样
        buffer.clear();
        System.out.println(getContent(buffer));
    }

    private static String getContent(ByteBuffer buffer) {
        return "内容为：" + new String(buffer.array()) + " capacity为：" + buffer.capacity() + " position为：" + buffer.position() + " limit为：" + buffer.limit() + " mark为：" + buffer.mark();
    }

}