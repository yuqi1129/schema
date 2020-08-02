package com.yuqi.sql;

import com.google.common.collect.Lists;
import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.List;
import java.util.Objects;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 10/7/20 19:55
 **/
public class SlothTable extends AbstractQueryableTable {

    private String tableName;

    private SlothSchema schema;

    private String createTableString;

    private List<SlothColumn> columns;

    private RelDataType resultType;

    public void setColumns(List<SlothColumn> columns) {
        this.columns = columns;
    }

    public SlothTable() {
        super(Object[].class);
    }

    public SlothTable(String tableName) {
        this();
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public SlothSchema getSchema() {
        return schema;
    }

    public void setSchema(SlothSchema schema) {
        this.schema = schema;
    }

    @Override
    public <T> Queryable<T> asQueryable(QueryProvider queryProvider, SchemaPlus schema, String tableName) {
        return null;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {

        if (Objects.nonNull(resultType)) {
            return resultType;
        }

        List<RelDataType> relDataTypes = Lists.newArrayListWithCapacity(columns.size());
        List<String> columnNames = Lists.newArrayList();

        columns.forEach(cl -> {
            final String sqlTypeNameString = cl.getColumnType().getTypeName().toString();
            final SqlTypeName sqlTypeName = SqlTypeName.get(sqlTypeNameString);
            RelDataType relDataType = typeFactory.createSqlType(sqlTypeName);
            typeFactory.createTypeWithNullability(relDataType, cl.getColumnType().getNullable());

            relDataTypes.add(relDataType);
            columnNames.add(cl.getColumnName());
        });

        resultType = typeFactory.createStructType(relDataTypes, columnNames);
        return resultType;
    }
}
