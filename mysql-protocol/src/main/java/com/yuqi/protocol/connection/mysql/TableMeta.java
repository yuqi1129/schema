package com.yuqi.protocol.connection.mysql;

import com.google.common.collect.Lists;
import com.yuqi.protocol.meta.tables.pojos.Columns;
import com.yuqi.sql.SlothColumn;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothTable;
import com.yuqi.sql.sqlnode.type.SlothColumnType;
import org.apache.calcite.sql.SqlBasicTypeNameSpec;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.type.SqlTypeName;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep7;

import java.util.List;
import java.util.stream.Collectors;

import static com.yuqi.protocol.meta.Sloth.SLOTH;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 19/8/20 10:36
 **/
public class TableMeta {

    public static final TableMeta INSTANCE = new TableMeta(MysqlConnection.INSTANCE);
    private MysqlConnection mysqlConnection;

    public TableMeta(MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
    }

    public void addTable(String schema, SlothTable table) {
        final String tableName = table.getTableName();
        //add table
        final DSLContext dslContext = mysqlConnection.getDslContext();
        dslContext.insertInto(SLOTH.TABLES)
                .columns(SLOTH.TABLES.TABLE_CATALOG, SLOTH.TABLES.TABLE_SCHEMA, SLOTH.TABLES.TABLE_NAME)
                .values("def", schema, tableName).execute();

        //add column
        InsertValuesStep7 insertValues = dslContext.insertInto(SLOTH.COLUMNS)
                .columns(SLOTH.COLUMNS.TABLE_CATALOG, SLOTH.COLUMNS.TABLE_SCHEMA, SLOTH.COLUMNS.TABLE_NAME, SLOTH.COLUMNS.COLUMN_NAME,
                        SLOTH.COLUMNS.COLUMN_TYPE, SLOTH.COLUMNS.GENERATION_EXPRESSION, SLOTH.COLUMNS.ORDINAL_POSITION);

        final List<SlothColumn> columnList = table.getColumns();
        final int size = columnList.size();

        for (int i = 0; i < size; i++) {
            insertValues = insertValues.values("def", schema, tableName, columnList.get(i).getColumnName(),
                    columnList.get(i).getColumnType().toString(), "", i);
        }

        insertValues.execute();
    }


    public void deleteTable(String schema, String table) {
        final DSLContext dslContext = mysqlConnection.getDslContext();

        //delete from tables;
        dslContext.deleteFrom(SLOTH.TABLES)
                .where(SLOTH.TABLES.TABLE_SCHEMA.eq(schema).and(SLOTH.TABLES.TABLE_NAME.eq(table)))
                .execute();

        //delete from columns
        dslContext.deleteFrom(SLOTH.COLUMNS)
                .where(SLOTH.COLUMNS.TABLE_NAME.eq(table).and(SLOTH.COLUMNS.TABLE_NAME.eq(table)))
                .execute();
    }

    public List<SlothTable> getAllTableInDb(SlothSchema schema) {
        final String schemaName = schema.getSchemaName();
        final DSLContext dslContext = mysqlConnection.getDslContext();
        final List<Columns> columns = dslContext.selectFrom(SLOTH.COLUMNS)
                .where(SLOTH.COLUMNS.TABLE_SCHEMA.eq(schemaName))
                .fetchInto(Columns.class);


        List<SlothTable> result = Lists.newArrayList();
        columns.stream().collect(Collectors.groupingBy(Columns::getTableName)).forEach((k, v) -> {
            final String tableName = k;
            final List<SlothColumn> slothColumns = v.stream().map(c -> {
                final SqlBasicTypeNameSpec sqlBasicTypeNameSpec = new SqlBasicTypeNameSpec(
                        SqlTypeName.valueOf(c.getColumnType()),
                        SqlParserPos.ZERO);

                //这里关于列、表组需要好好重组一下
                SqlDataTypeSpec dataTypeSpec = new SqlDataTypeSpec(sqlBasicTypeNameSpec, SqlParserPos.ZERO);
                dataTypeSpec = dataTypeSpec.withNullable(true);
                final SqlDataTypeSpec sqlDataTypeSpec = new SlothColumnType(dataTypeSpec, null, false, null, null);
                return new SlothColumn(c.getColumnName(), sqlDataTypeSpec);
            }).collect(Collectors.toList());

            SlothTable slothTable = new SlothTable(tableName);
            slothTable.setColumns(slothColumns);
            slothTable.setSchema(schema);
            slothTable.initTableEngine();
            result.add(slothTable);
        });


        return result;
    }
}
