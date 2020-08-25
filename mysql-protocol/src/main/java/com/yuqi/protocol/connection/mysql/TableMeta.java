package com.yuqi.protocol.connection.mysql;

import com.google.common.collect.Lists;
import com.yuqi.protocol.meta.tables.pojos.Columns;
import com.yuqi.protocol.meta.tables.pojos.Tables;
import com.yuqi.sql.EnhanceSlothColumn;
import com.yuqi.sql.SlothColumn;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep10;

import java.util.List;
import java.util.Objects;
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
                .columns(SLOTH.TABLES.TABLE_CATALOG,
                        SLOTH.TABLES.TABLE_SCHEMA,
                        SLOTH.TABLES.TABLE_NAME,
                        SLOTH.TABLES.TABLE_COMMENT,
                        SLOTH.TABLES.ENGINE, SLOTH.TABLES.TABLE_SHARD)
                .values("def",
                        schema,
                        tableName,
                        Objects.isNull(table.getTableComment()) ? "" : table.getTableComment(),
                        Objects.isNull(table.getEngineName()) ? null : table.getEngineName(),
                        table.getShardNum())
                .execute();

        //add column
        InsertValuesStep10 insertValues = dslContext.insertInto(SLOTH.COLUMNS)
                .columns(SLOTH.COLUMNS.TABLE_CATALOG,
                        SLOTH.COLUMNS.TABLE_SCHEMA,
                        SLOTH.COLUMNS.TABLE_NAME,
                        SLOTH.COLUMNS.COLUMN_NAME,
                        SLOTH.COLUMNS.COLUMN_TYPE,
                        SLOTH.COLUMNS.GENERATION_EXPRESSION,
                        SLOTH.COLUMNS.ORDINAL_POSITION,
                        SLOTH.COLUMNS.COLUMN_DEFAULT,
                        SLOTH.COLUMNS.IS_NULLABLE,
                        SLOTH.COLUMNS.COLUMN_COMMENT
                );

        final List<SlothColumn> columnList = table.getColumns();
        final int size = columnList.size();

        for (int i = 0; i < size; i++) {
            insertValues = insertValues.values(
                    "def",
                    schema,
                    tableName,
                    columnList.get(i).getColumnName(),
                    columnList.get(i).getColumnType().getColumnType().getName(),
                    "",
                    i,
                    columnList.get(i).getColumnType().getDefalutValue(),
                    columnList.get(i).getColumnType().isNullable() ? "YES" : "NO",
                    Objects.isNull(columnList.get(i).getColumnType().getColumnComment()) ? "" : columnList.get(i).getColumnType().getColumnComment()
            );
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
        columns.stream().collect(Collectors.groupingBy(Columns::getTableName)).forEach((tableName, cols) -> {
            final List<SlothColumn> slothColumns = cols.stream().map(c -> {
                EnhanceSlothColumn enhanceSlothColumn = new EnhanceSlothColumn();
                enhanceSlothColumn.setNullable(!Objects.equals("NO", c.getIsNullable()));
                enhanceSlothColumn.setColumName(c.getColumnName());
                enhanceSlothColumn.setColumnComment(c.getColumnComment());
                enhanceSlothColumn.setColumnType(SqlTypeName.valueOf(c.getColumnType()));
                enhanceSlothColumn.setDefalutValue(c.getColumnDefault());
                return new SlothColumn(c.getColumnName(), enhanceSlothColumn);
            }).collect(Collectors.toList());

            SlothTable slothTable = new SlothTable(tableName);

            final List<Tables> tables =
                    dslContext.selectFrom(SLOTH.TABLES)
                            .where(SLOTH.TABLES.TABLE_NAME.eq(tableName).and(SLOTH.TABLES.TABLE_SCHEMA.eq(schemaName)))
                            .fetchInto(Tables.class);

            if (tables.size() != 1) {
                return;
            }
            final Tables t = tables.get(0);

            slothTable.setColumns(slothColumns);
            slothTable.setSchema(schema);
            slothTable.setShardNum(t.getTableShard());
            slothTable.setEngineName(t.getEngine());
            slothTable.setTableComment(t.getTableComment());
            slothTable.initTableEngine();
            result.add(slothTable);
        });

        return result;
    }
}
