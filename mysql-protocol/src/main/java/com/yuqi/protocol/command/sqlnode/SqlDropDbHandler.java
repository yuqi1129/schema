package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlDropDb;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 16:58
 **/
public class SqlDropDbHandler implements Handler<SqlDropDb> {

    public static final SqlDropDbHandler INSTANCE = new SqlDropDbHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlDropDb sqlNode) {
        //handle sql create db;
        final String db = sqlNode.getDbName();
        SlothSchemaHolder.INSTANCE.removeSchema(db);

        final MysqlPackage mysqlPackage =
                PackageUtils.buildOkMySqlPackage(1, 1, 0);
        final ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
        connectionContext.getChannelHandlerContext().writeAndFlush(byteBuf);
    }
}
