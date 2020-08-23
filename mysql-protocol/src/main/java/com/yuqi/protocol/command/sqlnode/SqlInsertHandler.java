package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.SlothTable;
import com.yuqi.storage.lucene.TableEngine;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlCharStringLiteral;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlNumericLiteral;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.COLUMN_COUNT_NOT_MATCH;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.COLUMN_EXIST_TWICE;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.NO_DATABASE_SELECTED;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.TABLE_NOT_EXISTS;
import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.UNKONW_COLUMN_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 9/8/20 21:03
 **/
public class SqlInsertHandler implements Handler<SqlInsert> {

    public static final SqlInsertHandler INSTANCE = new SqlInsertHandler();
    public static final Logger LOGGER = LoggerFactory.getLogger(SqlInsertHandler.class);

    @Override
    public void handle(ConnectionContext connectionContext, SqlInsert type) {

        final SqlNode table = type.getTargetTable();
        final SqlNodeList columnList = type.getTargetColumnList();

        //TODO currently, do not suport insert into xxx select xxx from xxx;
        final List<SqlNode> operands = ((SqlBasicCall) type.getSource())
                .getOperandList();

        final String tableAndDb = table.toString();
        String[] dbAndDbArray = tableAndDb.split("\\.", 2);
        String db = dbAndDbArray.length == 2 ? dbAndDbArray[0] : connectionContext.getDb();
        String tableName = dbAndDbArray.length == 2 ? dbAndDbArray[1] : dbAndDbArray[0];

        SlothTable slothTable = checkTableAndDb(connectionContext, db, tableName);
        if (Objects.isNull(slothTable)) {
            return;
        }

        final TableEngine tableEngine = slothTable.getTableEngine();
        final List<String> columnsNames = checkAndGetColumnName(connectionContext, tableEngine, columnList);
        if (Objects.isNull(columnsNames)) {
            return;
        }

        final List<List<Value>> valueList = extractColumnValue(connectionContext, operands,
                tableEngine, columnsNames);

        if (Objects.isNull(valueList)) {
            return;
        }

        tableEngine.insert(valueList);

        final MysqlPackage r = PackageUtils.buildOkMySqlPackage(valueList.size(), 1, 0);
        connectionContext.write(r);
    }

    private SlothTable checkTableAndDb(ConnectionContext connectionContext, String db, String tableName) {
        MysqlPackage r;

        //check whether has used db
        if (Objects.isNull(db)) {
            r = PackageUtils.buildErrPackage(NO_DATABASE_SELECTED.getCode(), NO_DATABASE_SELECTED.getMessage());
            connectionContext.write(r);
            return null;
        }

        //check whether db exists
        final SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        if (Objects.isNull(slothSchema)) {
            r = PackageUtils.buildErrPackage(NO_DATABASE_SELECTED.getCode(), NO_DATABASE_SELECTED.getMessage());
            connectionContext.write(r);
            return null;
        }

        //check whether table exists
        final SlothTable slothTable = (SlothTable) slothSchema.getTable(tableName);
        if (Objects.isNull(slothTable)) {
            r = PackageUtils.buildErrPackage(
                    TABLE_NOT_EXISTS.getCode(),
                    String.format(TABLE_NOT_EXISTS.getMessage(), db + "." + tableName));
            connectionContext.write(r);
            return null;
        }

        return slothTable;
    }

    private List<String> checkAndGetColumnName(ConnectionContext connectionContext,
                                               TableEngine tableEngine, SqlNodeList sqlNodes) {

        //insert to t values()....., do not check column name and size
        final List<String> allColumnNames = tableEngine.getColumnNames();
        if (Objects.isNull(sqlNodes)) {
            return allColumnNames;
        }

        final List<String> columnsNames = sqlNodes.getList().stream()
                .map(SqlNode::toString)
                .collect(Collectors.toList());


        final List<String> unknowColumns = ListUtils.removeAll(columnsNames, allColumnNames);

        //insert into t(c1, c2) values(1, 2) and c1 does not exsit in table
        if (CollectionUtils.isNotEmpty(unknowColumns)) {
            MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(
                    UNKONW_COLUMN_NAME.getCode(),
                    String.format(UNKONW_COLUMN_NAME.getMessage(), unknowColumns.toString()));

            connectionContext.write(mysqlPackage);
            return null;
        }

        //insert into t(c1, c2) values(2, 2) c1 exists twice
        List<Pair<String, Integer>> pairs = columnsNames.stream()
                .collect(Collectors.groupingBy(f -> f))
                .entrySet()
                .stream()
                .map(entry -> new ImmutablePair<>(entry.getKey(), entry.getValue().size()))
                .filter(immutablePair -> immutablePair.right > 1)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(pairs)) {
            MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(
                    UNKONW_COLUMN_NAME.getCode(),
                    String.format(COLUMN_EXIST_TWICE.getMessage(), pairs.get(0).getLeft()));

            connectionContext.write(mysqlPackage);
            return null;
        }

        return columnsNames;
    }

    private List<List<Value>> extractColumnValue(ConnectionContext connectionContext,
                                                 List<SqlNode> rows,
                                                 TableEngine tableEngine,
                                                 List<String> columnList) {

        final List<List<Value>> rs = Lists.newArrayList();

        final List<String> allColumns = tableEngine.getColumnNames();
        final Map<String, DataType> map = tableEngine.getColumnAndDataType();

        int rowCount = rows.size();
        for (int i = 0; i < rowCount; i++) {
            SqlBasicCall basicCall = (SqlBasicCall) rows.get(i);

            //do not support insert into select syntax
            if (basicCall.getOperator() != SqlStdOperatorTable.ROW) {
                continue;
            }

            final List<SqlNode> rowValues = basicCall.getOperandList();
            int dataColumnSize = rowValues.size();
            int columnSize = columnList.size();

            if (dataColumnSize != columnSize) {
                MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(
                        COLUMN_COUNT_NOT_MATCH.getCode(),
                        String.format(COLUMN_COUNT_NOT_MATCH.getMessage(), i + 1));
                connectionContext.write(mysqlPackage);
                return null;
            }

            final Map<String, Value> columnNameAndValue = Maps.newHashMap();
            for (int j = 0; j < columnSize; j++) {
                final String colName = columnList.get(j);
                final DataType dataType = map.get(colName);

                SqlNode node = rowValues.get(j);
                Value v;

                //todo Should check value and type conflict
                if (node instanceof SqlNumericLiteral) {
                    BigDecimal decimal = (BigDecimal) ((SqlNumericLiteral) node).getValue();
                    v = dataType.createByType(decimal);
                } else {
                    //todo
                    SqlCharStringLiteral sqlCharStringLiteral = (SqlCharStringLiteral) node;
                    final String stringValue = sqlCharStringLiteral.getNlsString().getValue();
                    v = dataType.createByType(stringValue);
                }

                columnNameAndValue.put(colName, v);
            }

            List<Value> row = Lists.newArrayList();
            for (String columnName : allColumns) {
                Value v = columnNameAndValue.get(columnName);
                if (Objects.isNull(v)) {
                    DataType dataType = map.get(columnName);
                    v = dataType.createByType(null);
                }

                row.add(v);
            }

            rs.add(row);
        }

        return rs;
    }
}
