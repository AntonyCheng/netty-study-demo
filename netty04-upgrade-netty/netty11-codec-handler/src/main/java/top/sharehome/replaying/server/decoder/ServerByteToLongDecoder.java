package top.sharehome.replaying.server.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务端Byte转Long类型解码器
 *
 * @author AntonyCheng
 */
public class ServerByteToLongDecoder extends ReplayingDecoder<Void> {
    /**
     * 解码方法
     *
     * @param ctx 上下文对象
     * @param in  入站的ByteBuf
     * @param out List集合。将解码后的数据传给下一个Handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(LocalDateTime.now() + "服务端正在解码数据...");
        // Long 是8个字节，所以需要判断in是否大于8，大于则表示能够拿到一个Long值
        // 此时就不需要进行判断，直接放置即可
        //if (in.readableBytes() >= 8) {
        //    // 读取出一个Long
        //    long l = in.readLong();
        //    // 加入List集合
        //    out.add(l);
        //}
        out.add(in.readLong());
    }
}
