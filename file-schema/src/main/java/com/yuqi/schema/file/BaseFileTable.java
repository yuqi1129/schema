package com.yuqi.schema.file;

import com.yuqi.schema.common.enumerator.BasicEnumerator;
import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.adapter.enumerable.EnumerableTableScan;
import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTableQueryable;

import java.util.Iterator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 15:56
 **/
public abstract class BaseFileTable extends AbstractQueryableTable implements TranslatableTable {

    private final AbstractFileReader fileReader;

    public BaseFileTable(AbstractFileReader fileReader) {
        super(Object[].class);
        this.fileReader = fileReader;
    }

    @Override
    public <T> Queryable<T> asQueryable(QueryProvider queryProvider, SchemaPlus schema, String tableName) {
        return new AbstractTableQueryable<T>(queryProvider, schema, this, tableName) {
            @Override
            public Enumerator<T> enumerator() {
                Iterator<Object[]> it = fileReader.readData();
                return (Enumerator<T>) new BasicEnumerator(it);
            }
        };
    }

    @Override
    public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
        return new EnumerableTableScan(context.getCluster(),
                context.getCluster().traitSetOf(EnumerableConvention.INSTANCE),
                relOptTable, (Class) getElementType());
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        return fileReader.getRowType(typeFactory);
    }
}
