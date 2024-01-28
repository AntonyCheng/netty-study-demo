package top.sharehome.traditional_io;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 传统IO文件拷贝客户端
 *
 * @author AntonyCheng
 */

public class CopyClient {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        String filePath = PROJECT_PATH + "/netty2-nio-demo/nio8-zero-copy/src/main/java/top/sharehome/traditional_io/file/1.txt";
        Socket socket = new Socket("127.0.0.1", 9999);
        byte[] bytes = new byte[4096];
        FileInputStream fileInputStream = new FileInputStream(filePath);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        int totalCount = 0;
        int readCount = 0;
        while ((readCount = fileInputStream.read(bytes)) != -1) {
            totalCount += readCount;
            dataOutputStream.write(bytes);
        }
        System.out.println("发送完成，一共 " + totalCount + " 个字节");
        dataOutputStream.close();
        outputStream.close();
        fileInputStream.close();
    }

}
