package top.sharehome.replaying.client.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.time.LocalDateTime;

/**
 * 客户端Long转Byte类型编码码器
 *
 * @author AntonyCheng
 */
public class ClientLongToByteEncoder extends MessageToByteEncoder<Long> {
    /**
     * 编码方法
     *
     * @param ctx 上下文对象
     * @param msg 出站的ByteBuf
     * @param out List集合。将编码后的数据传给下一个Handler
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println(LocalDateTime.now() +"客户端正在编码数据...");
        out.writeLong(msg);
    }
}
