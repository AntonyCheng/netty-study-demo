package top.sharehome.demo02_solve.client.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.sharehome.demo02_solve.MessageProtocol;

/**
 * 客户端编码器
 *
 * @author AntonyCheng
 */
public class ClientEncoder extends MessageToByteEncoder<MessageProtocol> {
    /**
     * 编码从处理器发送过来的自定义消息协议，并且发送
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        // 向下游缓冲区写入消息长度
        out.writeInt(msg.getLen());
        // 向下游缓冲区写入消息内容
        out.writeBytes(msg.getContent());
    }
}
