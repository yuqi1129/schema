package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.netty.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.result.ErrorMessage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.EnhanceSlothColumn;
import com.yuqi.sql.SlothColumn;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.SlothTable;
import com.yuqi.sql.sqlnode.ddl.SqlCreateTable;
import com.yuqi.sql.sqlnode.type.SlothColumnType;
import com.yuqi.util.StringUtil;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.commons.lang3.tuple.Pair;

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
        final String tableName = type.getTableName();
        final boolean isNotExist = type.isNotExisted();
        final String db = connectionContext.getDb();

        final ErrorMessage errorMessage = checkDbAndTableName(tableName, db, isNotExist);

        if (ErrorMessage.OK_MESSAGE_AND_RETURN == errorMessage) {
            MysqlPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(0, 1, 0);
            connectionContext.write(mysqlPackage);
            return;
        }

        if (ErrorMessage.OK_MESSAGE != errorMessage) {
            MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(errorMessage.getErrorCode(), errorMessage.getDetailMessage());
            connectionContext.write(mysqlPackage);
            return;
        }

        //now handle
        createTable(tableName, type, connectionContext);
    }


    private void createTable(String tableAndDb, SqlCreateTable sqlCreateTable, ConnectionContext connectionContext) {
        final SqlNodeList sqlNodes = sqlCreateTable.getNameAndType();

        final Pair<String, String> dbAndTablePair = StringUtil.getDbAndTablePair(tableAndDb, connectionContext.getDb());
        final String db = dbAndTablePair.getLeft();
        final String tableName = dbAndTablePair.getRight();

        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        final SlothTable slothTable = new SlothTable(slothSchema);
        slothTable.setTableName(tableName);

        final List<SqlNode> nodes = sqlNodes.getList();
        final int size = nodes.size();

        final List<SlothColumn> slothColumns = Lists.newArrayList();
        for (int i = 0; i < size / 2; i++) {
            final String columnName = nodes.get(2 * i).toString();
            final SlothColumnType sqlTypeName = (SlothColumnType) nodes.get(2 * i + 1);
            final EnhanceSlothColumn column = sqlTypeName.toEnhance(columnName);
            final SlothColumn slothColumn = new SlothColumn(columnName, column);

            slothColumns.add(slothColumn);
        }

        slothTable.setShardNum(sqlCreateTable.getShard());
        slothTable.setColumns(slothColumns);

        if (Objects.nonNull(sqlCreateTable.getEngine())) {
            slothTable.setEngineName(sqlCreateTable.getEngine());
        }

        if (Objects.nonNull(sqlCreateTable.getTableComment())) {
            slothTable.setTableComment(sqlCreateTable.getTableComment().toString());
        }

        slothTable.initTableEngine();
        slothSchema.addTable(tableName, slothTable);

        final MysqlPackage result =
                PackageUtils.buildOkMySqlPackage(0, 1, 0);
        connectionContext.write(result);
    }


    private ErrorMessage checkDbAndTableName(String tableNameAndDB, String db, boolean isNotExist) {
        final Pair<String, String> dbAndTablePair = StringUtil.getDbAndTablePair(tableNameAndDB, db);

        final String realDb = dbAndTablePair.getLeft();
        final String tableName = dbAndTablePair.getRight();


        if (Objects.isNull(realDb)) {
            return new ErrorMessage(NO_DATABASE_SELECTED.getCode(), NO_DATABASE_SELECTED.getMessage());
        }

        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        if (Objects.isNull(slothSchema)) {
            return new ErrorMessage(UNKNOWN_DB_NAME.getCode(), String.format(UNKNOWN_DB_NAME.getMessage(), realDb));
        }

        if (slothSchema.containsTable(tableName)) {
            if (isNotExist) {
                return ErrorMessage.OK_MESSAGE_AND_RETURN;
            } else {
                return new ErrorMessage(TABLE_ALREADY_EXISTS.getCode(),
                        String.format(TABLE_ALREADY_EXISTS.getMessage(), tableNameAndDB));
            }
        }

        return ErrorMessage.OK_MESSAGE;
    }
}
