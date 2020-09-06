package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.netty.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.env.SlothEnvironmentValueHolder;
import com.yuqi.sql.sqlnode.ddl.SqlSet;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/9/20 21:48
 **/
public class SqlSetHandler implements Handler<SqlSet> {

    public static final SqlSetHandler INSTANCE = new SqlSetHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlSet type) {
        final String key = type.getKey();
        final String value = type.getValue();

        final boolean isGlobal = type.isGobal();

        //TODO Currently, we do not check the key and value is a valid key or value;
        if (isGlobal) {
            SlothEnvironmentValueHolder.INSTACNE.add(key, value);
        } else {
            connectionContext.getProperties().put(key, value);
        }

        final MysqlPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(0, 1, 0);
        connectionContext.write(mysqlPackage);
    }
}
