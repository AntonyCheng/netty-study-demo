package top.sharehome.otherapis;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path类API示例代码
 * 主要针对路径进行操作，具体操作请查看对象可执行的API
 *
 * @author AntonyCheng
 */
public class Demo01Path {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 方法入口
     */
    public static void main(String[] args) {
        // 1、以绝对路径创建Path对象
        Path absolutePath = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/file/1.txt");
        // 2、以相对路径创建Path对象，这里需要填写两个参数：基础路径，相对于基础路径的相对路径
        Path relativePath = Paths.get(PROJECT_PATH, "netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/file/1.txt");
        // 3、路径标准化（了解即可，会使用到的概率极小）
        Path notStandardPath = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/../1.txt");
        System.out.println("不标准的路径：" + notStandardPath);
        Path standardPath = notStandardPath.normalize();
        System.out.println("标准的路径：" + standardPath);
    }

}
