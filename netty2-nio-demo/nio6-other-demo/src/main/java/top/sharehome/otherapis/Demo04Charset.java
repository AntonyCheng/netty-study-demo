package top.sharehome.otherapis;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

/**
 * 字符集（Charset）示例代码
 *
 * @author AntonyCheng
 */
public class Demo04Charset {

    /**
     * 方法入口
     */
    public static void main(String[] args) throws CharacterCodingException {
        // 1、获取Charset对象，但是现在也可用 Charset charset = StandardCharsets.UTF_8; 局限性就在于只能使用StandardCharsets中固定的字符集；
        Charset charset = Charset.forName("UTF-8");

        // 2、获取编码器对象
        CharsetEncoder charsetEncoder = charset.newEncoder();

        // 3、创建包含一些内容的写模式缓冲区
        CharBuffer buffer = CharBuffer.wrap("你好！世界！");

        // 4、编码，获得编码之后的读模式ByteBuffer
        ByteBuffer encodeBuffer = charsetEncoder.encode(buffer);
        System.out.println("编码之后的Buffer：");
        System.out.println(new String(encodeBuffer.array()));

        // 5、获取解码器对象
        CharsetDecoder charsetDecoder = charset.newDecoder();

        // 6、解码，要求解码buffer为“输入流”，即读模式
        CharBuffer charBuffer = charsetDecoder.decode(encodeBuffer);
        System.out.println("解码之后的Buffer：");
        System.out.println(charBuffer);

        // 7、最后补充一下Charset支持的所有编码
        System.out.println("Charset中支持的字符集如下：");
        Charset.availableCharsets().forEach((charsetName, availableCharset) -> {
            System.out.println(charsetName + "==>" + availableCharset);
        });
    }

}
