package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.result.ErrorMessage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothColumn;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.SlothTable;
import com.yuqi.sql.sqlnode.ddl.SqlCreateTable;
import com.yuqi.sql.sqlnode.type.SlothColumnType;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;

import java.util.List;
import java.util.Objects;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.NO_DATABASE_SELECTED;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.TABLE_ALREADY_EXISTS;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.UNKNOWN_DB_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 2/8/20 16:19
 **/
public class SqlCreateTableHandler implements Handler<SqlCreateTable> {

    public static final SqlCreateTableHandler INSTANCE = new SqlCreateTableHandler();
    @Override
    public void handle(ConnectionContext connectionContext, SqlCreateTable type) {
        final SqlNodeList sqlNodes = type.getNameAndType();
        final String tableName = type.getTableName();
        final boolean isNotExist = type.isNotExisted();
        String db = connectionContext.getDb();

        final ErrorMessage errorMessage = checkDbAndTableName(tableName, db, isNotExist);
        if (ErrorMessage.OK_MESSAGE != errorMessage) {
            MysqlPackage mySQLPackage = PackageUtils.buildErrPackage(errorMessage.getErrorCode(), errorMessage.getDetailMessage(), 1);
            connectionContext.write(mySQLPackage);
            return;
        }

        //now handle
        createTable(tableName, sqlNodes, connectionContext);
    }


    private void createTable(String tableAndDb, SqlNodeList sqlNodes, ConnectionContext connectionContext) {
        final String[] tableAndDatabase = tableAndDb.split("\\.", 2);
        final String db;
        final String tableName;

        if (tableAndDatabase.length == 1) {
            db = connectionContext.getDb();
            tableName = tableAndDatabase[0];
        } else {
            db = tableAndDatabase[0];
            tableName = tableAndDatabase[1];
        }

        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        final SlothTable slothTable = new SlothTable(slothSchema);
        slothTable.setTableName(tableName);

        final List<SqlNode> nodes = sqlNodes.getList();
        final int size = nodes.size();

        final List<SlothColumn> slothColumns = Lists.newArrayList();
        for (int i = 0; i < size / 2; i++) {
            final String columnName = nodes.get(2 * i).toString();
            final SlothColumnType sqlTypeName = (SlothColumnType) nodes.get(2 * i + 1);
            final SlothColumn slothColumn = new SlothColumn(columnName, sqlTypeName);
            slothColumns.add(slothColumn);
        }

        slothTable.setColumns(slothColumns);
        slothTable.initTableEngine();

        slothSchema.addTable(tableName, slothTable);

        final MysqlPackage result =
                PackageUtils.buildOkMySqlPackage(0, 1, 0);
        connectionContext.write(result);
    }


    private ErrorMessage checkDbAndTableName(String tableNameAndDB, String db, boolean isNotExist) {
        final String[] tableAndDb = tableNameAndDB.split("\\.", 2);

        if (tableAndDb.length == 1) {
            if (Objects.isNull(db)) {
                return new ErrorMessage(NO_DATABASE_SELECTED.getCode(), NO_DATABASE_SELECTED.getMessage());
            }

            final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
            if (slothSchema.containsTable(tableNameAndDB) && !isNotExist) {
                return new ErrorMessage(TABLE_ALREADY_EXISTS.getCode(),
                        String.format(TABLE_ALREADY_EXISTS.getMessage(), tableNameAndDB));
            }

            return ErrorMessage.OK_MESSAGE;
        }

        final String realDb = tableAndDb[0];
        final String tableName = tableAndDb[1];

        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(realDb);
        if (Objects.isNull(slothSchema)) {
            return new ErrorMessage(UNKNOWN_DB_NAME.getCode(), String.format(UNKNOWN_DB_NAME.getMessage(), realDb));
        }

        if (slothSchema.containsTable(tableName) && !isNotExist) {
            return new ErrorMessage(TABLE_ALREADY_EXISTS.getCode(),
                    String.format(TABLE_ALREADY_EXISTS.getMessage(), tableName));
        }

        return ErrorMessage.OK_MESSAGE;
    }
}
