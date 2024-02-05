package top.sharehome.demo02_solve.client.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import top.sharehome.demo02_solve.MessageProtocol;

import java.util.List;

/**
 * 客户端编码器
 *
 * @author AntonyCheng
 */
public class ClientDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 将二进制字节转换成MessageProtocol对象
        // 先读取到长度
        int length = in.readInt();
        // 然后创建内容空数组
        byte[] content = new byte[length];
        // 再向空数组中读进内容
        in.readBytes(content);
        // 封装成MessageProtocol对象
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);
        // 将对象输入List中
        out.add(messageProtocol);
    }
}
