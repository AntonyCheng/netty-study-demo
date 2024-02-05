package top.sharehome.demo02_solve.server.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.sharehome.demo02_solve.MessageProtocol;

/**
 * 服务端编码器
 *
 * @author AntonyCheng
 */
public class ServerEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        // 向下游缓冲区写入消息长度
        out.writeInt(msg.getLen());
        // 向下游缓冲区写入消息内容
        out.writeBytes(msg.getContent());
    }
}
