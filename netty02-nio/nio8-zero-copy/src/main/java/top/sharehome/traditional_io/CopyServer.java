package top.sharehome.traditional_io;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统IO文件拷贝服务端
 *
 * @author AntonyCheng
 */

public class CopyServer {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("文件服务器启动成功...");
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream socketInputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(socketInputStream);
            byte[] bytes = new byte[4096];
            while (dataInputStream.read(bytes) != -1) {
                FileOutputStream fileOutputStream = new FileOutputStream(PROJECT_PATH + "/netty2-nio-demo/nio8-zero-copy/src/main/java/top/sharehome/traditional_io/file/2.txt");
                fileOutputStream.write(bytes);
            }
        }
    }

}
