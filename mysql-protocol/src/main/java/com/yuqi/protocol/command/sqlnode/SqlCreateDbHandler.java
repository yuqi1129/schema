package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlCreateDb;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.DATABASE_EXISTS_ERROR;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 16:54
 **/
public class SqlCreateDbHandler implements Handler<SqlCreateDb> {

    public static final SqlCreateDbHandler INSTANCE = new SqlCreateDbHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlCreateDb type) {
        final String db = type.getDbName();

        //db already exists
        if (SlothSchemaHolder.INSTANCE.contains(db)) {
            MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(
                    DATABASE_EXISTS_ERROR.getCode(),
                    String.format(DATABASE_EXISTS_ERROR.getMessage(), db),
                    1);
            connectionContext.write(mysqlPackage);
            return;
        }

        SlothSchemaHolder.INSTANCE.registerSchema(db);
        final MysqlPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(1, 1, 0);
        connectionContext.write(mysqlPackage);
    }
}
