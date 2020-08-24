package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.EnhanceSlothColumn;
import com.yuqi.sql.SlothColumn;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.SlothTable;
import com.yuqi.sql.sqlnode.ddl.SqlShow;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yuqi.protocol.constants.ColumnTypeConstants.MYSQL_TYPE_VAR_STRING;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.NO_DATABASE_SELECTED;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.SYNTAX_ERROR;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.TABLE_NOT_EXISTS;
import static com.yuqi.sql.SlothTable.DEFAULT_ENGINE_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 17:00
 **/
public class SqlShowHandler implements Handler<SqlShow> {

    public static final SqlShowHandler INSTANCE = new SqlShowHandler();

    private static final String CREATE_TABLE_RESULT_COLUMN1 = "Table";
    private static final String CREATE_TABLE_RESULT_COLUMN2 = "Create Table";

    @Override
    public void handle(ConnectionContext connectionContext, SqlShow type) {
        final String command = type.getCommand();
        //todo
        List<List<String>> data;
        String[] columnName = {"Database"};
        final int showType = type.getType();
        if (SqlShow.SHOW_DATABASES == showType) {
            data = SlothSchemaHolder.INSTANCE.getAllSchemas().stream()
                    .map(Lists::newArrayList)
                    .collect(Collectors.toList());

        } else if (SqlShow.SHOW_TABLES == showType) {
            String db = connectionContext.getDb();
            if (Objects.isNull(db)) {
                MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(
                        NO_DATABASE_SELECTED.getCode(),
                        NO_DATABASE_SELECTED.getMessage());

                connectionContext.write(mysqlPackage);
                return;
            }
            columnName = new String[] {String.join("_", Lists.newArrayList("Tables", "in", db))};

            final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
            data = slothSchema.getTables().stream().map(Lists::newArrayList).collect(Collectors.toList());
        } else if (SqlShow.SHOW_CREATE_TABLE == showType) {
            final ShowCreateTableResult showCreateTableResult = getCreateTable(command, connectionContext.getDb());
            if (showCreateTableResult.hasError) {
                connectionContext.write(showCreateTableResult.mysqlPackage);
                return;
            }

            data = showCreateTableResult.columnValues.stream().map(Lists::newArrayList).collect(Collectors.toList());
            columnName = showCreateTableResult.columnNames;
        } else {
            final MysqlPackage r = PackageUtils.buildErrPackage(
                    SYNTAX_ERROR.getCode(),
                    String.format(SYNTAX_ERROR.getMessage(), connectionContext.getQueryString()));

            connectionContext.write(r);
            return;
        }

        final List<Integer> columnTypes = Lists.newArrayList();
        for (int i = 0; i < columnName.length; i++) {
            columnTypes.add(MYSQL_TYPE_VAR_STRING);
        }

        final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                .columnName(columnName)
                .columnType(columnTypes)
                .data(data)
                .schema(StringUtils.EMPTY)
                .table(StringUtils.EMPTY)
                .build();

        final ByteBuf byteBuf = PackageUtils.buildResultSet(resultSetHolder);
        connectionContext.write(byteBuf);
    }

    private ShowCreateTableResult getCreateTable(String tableNameDatabase, String dbFromConnction) {
        final String[] tableAndDb = tableNameDatabase.split("\\.", 2);
        final String db = tableAndDb.length == 2 ? tableAndDb[0] : dbFromConnction;
        final String tableName = tableAndDb.length == 2 ? tableAndDb[1] : tableAndDb[0];

        MysqlPackage mysqlPackage = null;
        if (Objects.isNull(db)) {
            mysqlPackage = PackageUtils.buildErrPackage(
                    NO_DATABASE_SELECTED.getCode(),
                    NO_DATABASE_SELECTED.getMessage());

            return new ShowCreateTableResult(true, null, null, mysqlPackage);
        }

        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        if (Objects.isNull(slothSchema)) {
            mysqlPackage = PackageUtils.buildErrPackage(
                    TABLE_NOT_EXISTS.getCode(),
                    String.format(TABLE_NOT_EXISTS.getMessage(), tableAndDb));
            return new ShowCreateTableResult(true, null, null, mysqlPackage);
        }

        final SlothTable slothTable = (SlothTable) slothSchema.getTable(tableName);
        if (Objects.isNull(slothTable)) {
            mysqlPackage = PackageUtils.buildErrPackage(
                    TABLE_NOT_EXISTS.getCode(),
                    String.format(TABLE_NOT_EXISTS.getMessage(), tableAndDb));
            return new ShowCreateTableResult(true, null, null, mysqlPackage);
        }

        //now start to get re
        final String[] columnNames = {CREATE_TABLE_RESULT_COLUMN1, CREATE_TABLE_RESULT_COLUMN2};
        final List<String> datas = Lists.newArrayList(tableName, buildCreateTableSql(slothTable));

        return new ShowCreateTableResult(false, columnNames, datas, mysqlPackage);
    }

    private String buildCreateTableSql(SlothTable slothTable) {

        final StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE `").append(slothTable.getTableName()).append("` (\n");

        final List<SlothColumn> columns = slothTable.getColumns();
        final int length = columns.size();

        for (int i = 0; i < length; i++) {
            final SlothColumn column = columns.get(i);

            final EnhanceSlothColumn columnType = column.getColumnType();

            builder.append("  `").append(column.getColumnName()).append("` ");
            builder.append(columnType.getColumnType()).append(" ");

            if (columnType.isUnsigned()) {
                builder.append("UNSIGNED ");
            }

            if (!columnType.isNullable()) {
                builder.append("NOT NULL ");
            }

            if (Objects.nonNull(columnType.getDefalutValue())) {
                builder.append("DEFALUT ").append(columnType.getDefalutValue()).append(" ");
            }

            if (Objects.nonNull(columnType.getColumnComment())) {
                builder.append("COMMENT ").append(columnType.getColumnComment());
            }

            if (i != length - 1) {
                builder.append(",");
            }

            builder.append("\n");
        }

        builder.append(") ");

        String engineName = slothTable.getEngineName();
        if (Objects.isNull(engineName)) {
            engineName = DEFAULT_ENGINE_NAME;
        }
        builder.append("Engine = ").append(engineName);

        final String tableComment = slothTable.getTableComment();
        if (StringUtils.isNotBlank(tableComment)) {
            builder.append("COMMENT = '").append(tableComment).append("';");
        }

        return builder.toString();
    }

    static class ShowCreateTableResult {
        private boolean hasError;
        private String[] columnNames;
        private List<String> columnValues;

        /**
         * not null iff hasError is true
         */
        private MysqlPackage mysqlPackage;

        ShowCreateTableResult(boolean hasError, String[] columnNames, List<String> columnValues, MysqlPackage mysqlPackage) {
            this.hasError = hasError;
            this.columnNames = columnNames;
            this.columnValues = columnValues;
            this.mysqlPackage = mysqlPackage;
        }
    }
}
