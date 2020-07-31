package com.yuqi.protocol.handler;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.pkg.auth.LoginRequest;
import com.yuqi.protocol.pkg.MySQLPackage;
import com.yuqi.protocol.pkg.response.Err;
import com.yuqi.protocol.pkg.response.Ok;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.yuqi.protocol.constants.ErrorCodeConstants.PASSWORD_OR_USER_IS_WRONG;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 22:57
 **/
public class AuthencationHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //???
        Channel channel = ctx.channel();

        boolean hasAlreadyAuthencaition =
                NettyConnectionHandler.INSTANCE.getAlreadyAuthenChannels().containsKey(channel)
                        && channel.isOpen()
                        && channel.isActive();

        if (hasAlreadyAuthencaition) {
            //已经验证过的话，直接调用下一个handler
            super.channelRead(ctx, msg);

        } else {
            //do authentcaion
            boolean res = doAuthencation((MySQLPackage) msg);

            AbstractReaderAndWriter abstractReaderAndWriterPackage;
            MySQLPackage mySQLPackage = new MySQLPackage();

            ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(256);
            if (res) {
                NettyConnectionHandler.INSTANCE.getAlreadyAuthenChannels().put(channel, new ConnectionContext(ctx));
                abstractReaderAndWriterPackage = Ok.builder()
                        .header((byte) 0x00)
                        .serverStatus(0x0002)
                        .affectedRows(0)
                        .lastInsertId(0)
                        //.info("Welecome to mock server")
                        .build();
            } else {
                abstractReaderAndWriterPackage = Err.builder()
                        .header((byte) 0xff)
                        .errorCode(PASSWORD_OR_USER_IS_WRONG)
                        .errorMessage("Wrong username or password").build();
            }

            mySQLPackage.setAbstractReaderAndWriterPackage(abstractReaderAndWriterPackage);
            //在认证过程中
            //server----->client 0x00
            //client----->server 0x01
            //server----->client 0x02
            //此处是第三条认证消息
            mySQLPackage.setSeqNumber((byte) 0x02);
            mySQLPackage.write(buf);
            ctx.writeAndFlush(buf);


            if (!res) {
                //错误的话，直接关闭
                ctx.channel().close();
            }
        }
    }

    private boolean doAuthencation(MySQLPackage mySQLPackage) {
        final String userName = ((LoginRequest) mySQLPackage.getAbstractReaderAndWriterPackage()).getUserName();
        final String passwordHash = ((LoginRequest) mySQLPackage.getAbstractReaderAndWriterPackage()).getPasswordHash();

        return compareUsernameAndPassword(userName, passwordHash);
    }

    private boolean compareUsernameAndPassword(String userName, String password) {
        //todo
        return true;
    }
}
