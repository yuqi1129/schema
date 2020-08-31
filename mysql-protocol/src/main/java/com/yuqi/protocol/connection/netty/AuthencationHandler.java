package com.yuqi.protocol.connection.netty;

import com.yuqi.protocol.constants.ErrorCodeAndMessageEnum;
import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.pkg.auth.LoginRequest;
import com.yuqi.protocol.pkg.response.ErrPackage;
import com.yuqi.protocol.pkg.response.OkPackage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/8/20 09:34
 **/
public class AuthencationHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //???
        final Channel channel = ctx.channel();

        boolean hasAlreadyAuthencaition =
                NettyConnectionHandler.INSTANCE.getAlreadyAuthenChannels().containsKey(channel)
                        && channel.isOpen()
                        && channel.isActive();

        if (hasAlreadyAuthencaition) {
            //已经验证过的话，直接调用下一个handler
            super.channelRead(ctx, msg);

        } else {
            //do authentcaion
            final LoginRequest loginRequest = (LoginRequest) ((MysqlPackage) msg)
                    .getAbstractReaderAndWriterPackage();
            boolean res = doAuthencation(loginRequest);

            AbstractReaderAndWriter abstractReaderAndWriterPackage;
            MysqlPackage mySQLPackage = new MysqlPackage();

            ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(256);
            if (res) {
                ConnectionContext connectionContext = new ConnectionContext(ctx);
                connectionContext.setDb(loginRequest.getDatabase());
                NettyConnectionHandler.INSTANCE.getAlreadyAuthenChannels().put(channel, connectionContext);
                abstractReaderAndWriterPackage = OkPackage.builder()
                        .header((byte) 0x00)
                        .serverStatus(0x0002)
                        .affectedRows(0)
                        .lastInsertId(0)
                        //.info("Welecome to mock server")
                        .build();
            } else {
                abstractReaderAndWriterPackage = ErrPackage.builder()
                        .header((byte) 0xff)
                        .errorCode((short) ErrorCodeAndMessageEnum.PASSWORD_OR_USER_IS_WRONG.getCode())
                        .errorMessage(ErrorCodeAndMessageEnum.PASSWORD_OR_USER_IS_WRONG.getMessage()).build();
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

    private boolean doAuthencation(LoginRequest loginRequest) {
        final String userName = loginRequest.getUserName();
        final String passwordHash = loginRequest.getAuthResponse();

        return compareUsernameAndPassword(userName, passwordHash);
    }

    private boolean compareUsernameAndPassword(String userName, String password) {
        //todo
        return true;
    }
}
