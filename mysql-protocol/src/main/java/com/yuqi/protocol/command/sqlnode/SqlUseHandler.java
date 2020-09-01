package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.netty.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.sqlnode.ddl.SqlUse;

import java.util.Objects;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.UNKNOWN_DB_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 17:02
 **/
public class SqlUseHandler implements Handler<SqlUse> {

    public static final SqlUseHandler INSTANCE = new SqlUseHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlUse type) {
        final String db = type.getDb();
        final MysqlPackage mysqlPackage = useDb(connectionContext, db);
        connectionContext.write(mysqlPackage);
    }

    public MysqlPackage useDb(ConnectionContext connectionContext, String db) {
        //check if schema contains db name;
        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        if (Objects.isNull(slothSchema)) {
            return PackageUtils.buildErrPackage(
                    UNKNOWN_DB_NAME.getCode(),
                    String.format(UNKNOWN_DB_NAME.getMessage(), db));
        }

        connectionContext.setDb(db);
        return PackageUtils.buildOkMySqlPackage(0, 1, 0);
    }
}
