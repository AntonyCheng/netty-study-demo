package top.sharehome.otherapis;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Files类API示例代码
 * 主要针对文件进行操作，经常和Path搭配使用
 *
 * @author AntonyCheng
 */
public class Demo02Files {

    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 方法入口
     */
    public static void main(String[] args) throws IOException {
        Path path = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/path");
        // 创建单级文件夹，如果目录已经存在，则会抛出异常
        Files.createDirectory(path);

        // 创建多级文件夹，即使目录已经存在也不会抛出异常
        Files.createDirectories(Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/path/subpath/subsubpath"));

        // 拷贝文件，需要源文件的Path对象以及目标文件的Path对象，如果目标文件已经存在或者源文件不存在，那么就会抛出异常
        Path path1 = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/file/1.txt");
        Path path2 = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/path/2.txt");
        Files.copy(path1, path2);
        // 如果不想目标文件已存在时抛出异常，可以增加copy的参数
        Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);

        // 移动文件（重命名），需要源文件的Path对象以及目标文件的Path对象，如果目标文件已经存在或者源文件不存在，那么就会抛出异常
        Path path3 = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/path/subpath/2.txt");
        Path path4 = Paths.get(PROJECT_PATH + "/netty2-nio-demo/nio6-other/src/main/java/top/sharehome/otherapis/path/subpath/3.txt");
        Files.move(path2, path3);
        // 如果不想目标文件已存在时抛出异常，可以增加copy的参数
        Files.move(path3, path4, StandardCopyOption.REPLACE_EXISTING);

        // 删除文件，但是目标不存在的话会保存
        Files.delete(path4);
        // 如果不想目标代码不存在时抛出异常，可以使用deleteIfExists
        Files.deleteIfExists(path4);

        // 遍历文件夹
        Files.copy(path1, path2);
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            // 进入文件夹之前的操作
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("在进入 " + dir + " 文件夹之前做的事情");
                return super.preVisitDirectory(dir, attrs);
            }

            // 跳出文件夹之后的操作
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("在跳出 " + dir + " 文件夹之后做的事情");
                return super.postVisitDirectory(dir, exc);
            }

            // 找到文件时的操作
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("已找到 " + file);
                return super.visitFile(file, attrs);
            }
        });
    }

}
