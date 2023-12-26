package top.sharehome;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 示例代码
 * BIO即同步阻塞型IO，也称传统IO，针对于网络连接来说就是一个连接一个线程
 * 1、 使用BIO模型编写一个服务器端，监听6666端口，当有客户端连接时，就启动一个线程与之通讯；
 * 2、 要求使用线程池机制改善，可以连接多个客户端；
 * 3、 服务器端可以接收客户端发送的数据（telnet实现即可）；
 * 测试方法：进入CMD输入"telnet 127.0.0.1 6666"，然后快捷键进入消息发送页面：ctrl+]，使用"sent XXX"命令发送消息；
 *
 * @author AntonyCheng
 */
public class Main {

    /**
     * 主方法
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // 创建一个连接池，如果有客户端链接就创建一个线程与之通讯
        ExecutorService threadPool = Executors.newCachedThreadPool(new DefaultThreadFactory("bio"));
        System.out.println("线程池创建成功！");
        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动成功！");
        while (true) {
            System.out.println("线程信息 id = "+Thread.currentThread().getId()+"，名称 = "+Thread.currentThread().getName());
            // 监听，等待客户端连接
            System.out.println("等待连接...");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端！");
            // 创建一个线程与之通讯
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    /**
     * 和客户端通讯
     *
     * @param socket
     */
    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息 id = "+Thread.currentThread().getId()+"，名称 = "+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("关闭一个客户端连接！");
        }
    }

}