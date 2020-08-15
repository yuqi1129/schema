package com.yuqi.protocol.connection.netty;

import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/8/20 09:41
 **/
//TODO have bug currently, do not use now
public class MysqlPackageToByteBufEncoder extends MessageToMessageEncoder<ByteBuf> {

    private ThreadLocal<MysqlByteBufMessage> byteBufThreadLocal = new ThreadLocal<>();

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        MysqlByteBufMessage mysqlByteBufMessage = byteBufThreadLocal.get();
        if (Objects.isNull(mysqlByteBufMessage)) {
            ByteBuf byteBuf = ctx.alloc().buffer(128);
            final int lengthOfMessage = IOUtils.readInteger(msg, 3);
            final byte seq = IOUtils.readByte(msg);
            IOUtils.writeInteger3(lengthOfMessage, byteBuf);
            IOUtils.writeByte(seq, byteBuf);
            mysqlByteBufMessage = new MysqlByteBufMessage(lengthOfMessage + 4, byteBuf);
            byteBufThreadLocal.set(mysqlByteBufMessage);
        }


        int i = 0;
        ByteBuf byteBuf = mysqlByteBufMessage.byteBuf;
        int requiredLength = mysqlByteBufMessage.requiredLength;
        int msgLen = msg.readableBytes();
        while (i < msgLen) {
            byteBuf.writeByte(msg.readByte());
            i++;

            if (byteBuf.readableBytes() == requiredLength) {
                out.add(IOUtils.copyByteBuf(byteBuf));
                byteBufThreadLocal.set(null);


                //TODO 这里不能一次读三个，很有可能读越界
                if (msg.readableBytes() != 0) {
                    byteBuf = ctx.alloc().buffer(128);
                    final int lengthOfMessage = IOUtils.readInteger(msg, 3);
                    final byte seq = IOUtils.readByte(msg);

                    IOUtils.writeInteger3(lengthOfMessage, byteBuf);
                    IOUtils.writeByte(seq, byteBuf);

                    i = i + 4;

                    mysqlByteBufMessage = new MysqlByteBufMessage(lengthOfMessage + 4, byteBuf);
                    byteBufThreadLocal.set(mysqlByteBufMessage);
                }
            }
        }
    }

    static class MysqlByteBufMessage {
        private int requiredLength;
        private ByteBuf byteBuf;

        MysqlByteBufMessage(int requiredLength, ByteBuf byteBuf) {
            this.requiredLength = requiredLength;
            this.byteBuf = byteBuf;
        }
    }
}
