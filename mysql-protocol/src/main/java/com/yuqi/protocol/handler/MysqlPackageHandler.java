package com.yuqi.protocol.handler;

import com.yuqi.protocol.command.CommandHandler;
import com.yuqi.protocol.command.QueryCommandHandler;
import com.yuqi.protocol.command.ShowCommandHandler;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MySQLPackage;
import com.yuqi.protocol.pkg.request.CommandPackage;
import com.yuqi.protocol.pkg.request.QueryPackage;
import com.yuqi.protocol.pkg.request.ShowPackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.yuqi.protocol.constants.CommandTypeConstants.COM_QUERY;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 21:11
 **/
public class MysqlPackageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final MySQLPackage mySQLPackage = (MySQLPackage) msg;
        final ConnectionContext connectionContext = NettyConnectionHandler.INSTANCE.getAlreadyAuthenChannels().get(ctx.channel());
        dispatchCommandPackage((CommandPackage) mySQLPackage.getAbstractPackage(), connectionContext);
    }

    private void dispatchCommandPackage(CommandPackage commandPackage, ConnectionContext connectionContext) {
        final byte type = commandPackage.getCommandType();
        CommandHandler handler;
        switch (type) {
            case COM_QUERY:
                handler = new QueryCommandHandler(connectionContext, (QueryPackage) commandPackage.getAbstractPackage());
                break;

            default:
                //todo
                handler = new ShowCommandHandler(connectionContext, (ShowPackage) commandPackage.getAbstractPackage());
        }

        handler.execute();
    }
}
