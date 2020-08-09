package com.yuqi.protocol.connection.netty;

import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.pkg.auth.LoginRequest;
import com.yuqi.protocol.pkg.request.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 22:21
 **/
public class ByteBufToPackageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final MysqlPackage result = new MysqlPackage();

        if (NettyConnectionHandler.INSTANCE.channelHasAuthencation(channelHandlerContext.channel())) {
            result.setAbstractReaderAndWriterPackage(new Command());
        } else {
            result.setAbstractReaderAndWriterPackage(new LoginRequest());
        }

        result.read(byteBuf);
        list.add(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);


    }
}
