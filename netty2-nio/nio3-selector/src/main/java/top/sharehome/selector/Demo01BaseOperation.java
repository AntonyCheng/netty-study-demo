package top.sharehome.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector基础操作
 * 注意：
 * 1、与Selector一起使用时，Channel必须处于非阻塞模式下，否则将抛出异常IllegalBlockingModeException。
 * 这意味着，FileChannel不能与Selector一起使用，因为FileChannel不能切换到非阻塞模式，而套接字相关的所有的通道都可以。
 * 2、一个通道，并没有一定要支持所有的四种操作。比如服务器通道ServerSocketChannel支持Accept接受操作，而SocketChannel客户端通道则不支持。
 * 可以通过通道上的validOps()方法，来获取特定通道下所有支持的操作集合。
 * （1）ServerSocketChannel ==> SelectionKey.OP_ACCEPT
 * （2）SocketChannel ==> SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT
 * （3）DatagramSocket ==> SelectionKey.OP_READ | SelectionKey.OP_WRITE
 *
 * @author AntonyCheng
 */

public class Demo01BaseOperation {

    /**
     * 正常使用流程
     * 将Channel注册到Selector上
     */
    private static void normalUse() throws IOException {
        // 创建Selector
        Selector selector = Selector.open();

        // 创建一个通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 使用非阻塞
        serverSocketChannel.configureBlocking(false);

        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9999));

        // 查询通道支持的就绪状态
        int i = serverSocketChannel.validOps();

        // 将通道注册到选择器上
        serverSocketChannel.register(selector, i);

        // 查询已经就绪的通道操作
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        // 遍历集合，轮询操作
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            // 判断Key的就绪状态
            if (key.isAcceptable()) {
                // 接收就绪状态
            } else if (key.isConnectable()) {
                // 连接就绪状态
            } else if (key.isReadable()) {
                // 可读就绪状态
            } else if (key.isWritable()) {
                // 可写就绪状态
            }
            // 每次一定要移除迭代器next元素
            iterator.remove();
        }
    }

    /**
     * 选择器执行选择使用轮询，可能会造成调用线程进入阻塞状态，以下方法能够唤醒在select()方法中的阻塞线程
     * 1、wakeup()：该方法使得选择器上的第一个还没有返回的选择操作立即返回。如果当前没有进行中的选择操作，那么下一次对select()方法的一次调用将立即返回。
     * 2、close()：该方法使得任何一个在选择操作中阻塞的线程都被唤醒（类似wakeup()），同时使得注册到该Selector的所有Channel被注销，所有的键将被取消，但是Channel本身并不会关闭。
     */
    private static void stopSelect() throws IOException {
        // 创建一个Channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        // 创建一个Selector
        Selector selector = Selector.open();

        // 进行注册
        serverSocketChannel.register(selector,serverSocketChannel.validOps());

        // 不管选择结果与否直接让其返回
        selector.wakeup();
        // 开始选择
        selector.select();

    }

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        stopSelect();
    }

}
