package com.yuqi.protocol.connection.netty;

import com.yuqi.protocol.command.sqlnode.SqlUseHandler;
import com.yuqi.protocol.constants.ErrorCodeAndMessageEnum;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.pkg.auth.LoginRequest;
import com.yuqi.protocol.pkg.response.ErrPackage;
import com.yuqi.protocol.utils.PackageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Objects;

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

            MysqlPackage mysqlPackage;

            if (res) {
                final ConnectionContext connectionContext = new ConnectionContext(ctx);
                NettyConnectionHandler.INSTANCE.getAlreadyAuthenChannels().put(channel, connectionContext);

                final String dbName = loginRequest.getDatabase();
                if (Objects.nonNull(dbName)) {
                    //直接set, 如果db不存在的话，后面会关闭连接
                    connectionContext.setDb(loginRequest.getDatabase());
                    mysqlPackage = SqlUseHandler.INSTANCE.useDb(
                            connectionContext, loginRequest.getDatabase());
                } else {
                    mysqlPackage = PackageUtils.buildOkMySqlPackage(0, 2, 0);
                }
            } else {
                mysqlPackage = PackageUtils.buildErrPackage(
                        ErrorCodeAndMessageEnum.PASSWORD_OR_USER_IS_WRONG.getCode(),
                        ErrorCodeAndMessageEnum.PASSWORD_OR_USER_IS_WRONG.getMessage()
                );
            }

            //在认证过程中
            //server----->client 0x00
            //client----->server 0x01
            //server----->client 0x02
            //此处是第三条认证消息
            mysqlPackage.setSeqNumber((byte) 0x02);
            final ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
            ctx.writeAndFlush(byteBuf);

            if (!res || mysqlPackage.getAbstractReaderAndWriterPackage() instanceof ErrPackage) {
                //认证错误或者Db不存在的话，直接关闭
                ctx.channel().close();
            }
        }
    }

    private boolean doAuthencation(LoginRequest loginRequest) {
        final String userName = loginRequest.getUserName();
        final String passwordHash = loginRequest.getAuthResponse();

        return compareUsernameAndPassword(userName, passwordHash);
    }

    /**
     * See https://jin-yang.github.io/post/mysql-protocol.html
     * @param userName
     * @param password
     * @return
     */
    private boolean compareUsernameAndPassword(String userName, String password) {

        //todo
        return true;
    }
}
