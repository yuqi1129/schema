package com.yuqi.protocol.handler;

import com.google.common.base.Throwables;
import com.yuqi.protocol.pkg.MySQLPackage;
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
public class MessageToPackageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MySQLPackage mySQLPackage = new MySQLPackage();

        if (NettyConnectionHandler.INSTANCE.channelHasAuthencation(channelHandlerContext.channel())) {
            mySQLPackage.setAbstractReaderAndWriterPackage(new Command());
        } else {
            mySQLPackage.setAbstractReaderAndWriterPackage(new LoginRequest());
        }

        mySQLPackage.read(byteBuf);
        list.add(mySQLPackage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);

        System.out.println(Throwables.getStackTraceAsString(cause));

    }
}
