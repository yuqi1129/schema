package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ddl.SqlUse;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 17:02
 **/
public class SqlUseHandler implements Handler<SqlUse> {

    public static final SqlUseHandler INSTANCE = new SqlUseHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlUse sqlNode) {
        final String db = sqlNode.getDb();

        //check if schema contains db name;
        connectionContext.setDb(db);

        MysqlPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(0, 1, 0);
        ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
        connectionContext.getChannelHandlerContext().writeAndFlush(byteBuf);
    }
}
