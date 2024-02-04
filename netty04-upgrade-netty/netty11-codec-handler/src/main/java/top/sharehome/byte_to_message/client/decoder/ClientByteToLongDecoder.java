package top.sharehome.byte_to_message.client.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户端Byte转Long类型解码器
 *
 * @author AntonyCheng
 */
public class ClientByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(LocalDateTime.now() + "客户端正在解码数据...");
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
