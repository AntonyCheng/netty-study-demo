package top.sharehome.demo02_solve.server.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import top.sharehome.demo02_solve.MessageProtocol;

import java.util.List;

/**
 * 服务端解码器
 * 请务必继承ReplayingDecoder<Void>，该类型能够给开发者自动判断ByteBuf中应该获取的长度，不需要再写复杂的逻辑代码来校验长度。
 *
 * @author AntonyCheng
 */
public class ServerDecoder extends ReplayingDecoder<Void> {
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
