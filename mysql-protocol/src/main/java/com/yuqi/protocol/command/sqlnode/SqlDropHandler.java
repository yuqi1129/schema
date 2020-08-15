package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlDrop;

import java.util.Objects;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.DATABASE_NOT_EXIST_IN_DROP;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.NO_DATABASE_SELECTED;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.UNKNOWN_TABLE_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 16:58
 **/
public class SqlDropHandler implements Handler<SqlDrop> {

    public static final SqlDropHandler INSTANCE = new SqlDropHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlDrop type) {
        //handle sql create db;
        final String name = type.getName();
        final boolean isDb = type.isDropDb();

        if (isDb) {
            dropDb(name, connectionContext);
        } else {
            dropTable(name, connectionContext);
        }
    }


    private void dropDb(String dbName, ConnectionContext connectionContext) {
       final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(dbName);

       if (Objects.isNull(slothSchema)) {
           final MysqlPackage error = PackageUtils.buildErrPackage(
                   DATABASE_NOT_EXIST_IN_DROP.getCode(),
                   DATABASE_NOT_EXIST_IN_DROP.getMessage(), 1);

           connectionContext.write(error);
           return;
       }

        SlothSchemaHolder.INSTANCE.removeSchema(dbName);

        final MysqlPackage mysqlPackage =
                PackageUtils.buildOkMySqlPackage(1, 1, 0);

        connectionContext.write(mysqlPackage);
    }

    private void dropTable(String tableAndDb, ConnectionContext connectionContext) {
        final String[] tableAndDatabase = tableAndDb.split("\\.", 2);

        final String table;
        final String db;
        if (tableAndDatabase.length == 1) {
            table = tableAndDatabase[0];
            db = connectionContext.getDb();
        } else {
            table = tableAndDatabase[0];
            db = tableAndDatabase[1];
        }

        if (Objects.isNull(db)) {
            final MysqlPackage error = PackageUtils.buildErrPackage(
                    NO_DATABASE_SELECTED.getCode(),
                    NO_DATABASE_SELECTED.getMessage(), 1);
            connectionContext.write(error);
            return;
        }

        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        if (Objects.isNull(slothSchema) || !slothSchema.containsTable(table)) {
            final MysqlPackage error = PackageUtils.buildErrPackage(
                    UNKNOWN_TABLE_NAME.getCode(),
                    String.format(UNKNOWN_TABLE_NAME.getMessage(), tableAndDb), 1);
            connectionContext.write(error);
            return;
        }

        slothSchema.dropTable(table);
        final MysqlPackage mysqlPackage =
                PackageUtils.buildOkMySqlPackage(1, 1, 0);

        connectionContext.write(mysqlPackage);
    }
}
