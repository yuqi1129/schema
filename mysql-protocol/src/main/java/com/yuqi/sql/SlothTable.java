package com.yuqi.sql;

import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 10/7/20 19:55
 **/
public class SlothTable extends AbstractQueryableTable {

    private String tableName;

    private SlothSchema schema;

    protected SlothTable() {
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
        return null;
    }
}
