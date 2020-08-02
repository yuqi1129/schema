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
import com.yuqi.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;

import java.util.List;
import java.util.Objects;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.NO_DATABASE_SELECTED;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.TABLE_ALREADY_EXISTS;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.UNKNOWN_DB_NAME;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 2/8/20 16:19
 **/
public class SqlCreateTableHandler implements Handler<SqlCreateTable> {

    public static final SqlCreateTableHandler INSTANCE = new SqlCreateTableHandler();
    @Override
    public void handle(ConnectionContext connectionContext, SqlCreateTable sqlNode) {
        final SqlNodeList sqlNodes = sqlNode.getNameAndType();
        final String tableName = sqlNode.getTableName();
        final boolean isNotExist = sqlNode.isNotExisted();
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
        final SlothTable slothTable = new SlothTable();

        final List<SqlNode> nodes = sqlNodes.getList();
        final int size = nodes.size();

        final List<SlothColumn> slothColumns = Lists.newArrayList();
        for (int i = 0; i < size / 2; i++) {
            final String columnName = nodes.get(2 * i).toString();
            final SqlDataTypeSpec sqlTypeName = (SqlDataTypeSpec) nodes.get(2 * i + 1);
            final SlothColumn slothColumn = new SlothColumn(columnName, sqlTypeName);
            slothColumns.add(slothColumn);
        }

        slothTable.setSchema(slothSchema);
        slothTable.setTableName(tableName);
        slothTable.setColumns(slothColumns);

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

            return ErrorMessage.OK_MESSAGE;
        }

        final String realDb = tableAndDb[0];
        final String tableName = tableAndDb[1];

        SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(realDb);
        if (Objects.isNull(slothSchema)) {
            return new ErrorMessage(UNKNOWN_DB_NAME.getCode(), String.format(UNKNOWN_DB_NAME.getMessage(), realDb));
        }

        if (slothSchema.containsTable(tableName) && !isNotExist) {
            return new ErrorMessage(TABLE_ALREADY_EXISTS.getCode(), String.format(TABLE_ALREADY_EXISTS.getMessage(), tableName));

        }

        return ErrorMessage.OK_MESSAGE;
    }
}
