package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MySQLPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlCreateDb;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 16:54
 **/
public class SqlCreateDbHandler implements Handler<SqlCreateDb> {

    public static final SqlCreateDbHandler INSTANCE = new SqlCreateDbHandler();
    //todo add more show syntax support
    @Override
    public void handle(ConnectionContext connectionContext, SqlCreateDb sqlNode) {
        final String db = sqlNode.getDbName();
        SlothSchemaHolder.INSTANCE.registerSchema(db);

        MySQLPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(1, 1, 0);
        ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
        connectionContext.getChannelHandlerContext().writeAndFlush(byteBuf);
    }
}
