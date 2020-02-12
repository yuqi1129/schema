package com.yuqi.schema.mysql;

import com.google.common.collect.Lists;
import com.yuqi.schema.common.util.JavaTypeToSqlTypeConversion;
import lombok.Getter;
import org.apache.calcite.adapter.enumerable.EnumerableConvention;
import org.apache.calcite.adapter.enumerable.EnumerableTableScan;
import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TranslatableTable;
import org.apache.calcite.schema.impl.AbstractTableQueryable;
import org.apache.calcite.sql.SqlDialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:12
 **/
public class MysqlTable extends AbstractQueryableTable implements TranslatableTable {

    private MysqlReader mysqlReader;
    private RelToSqlConverter relToSqlConverter;
    private String schema;
    private String tableName;
    private Connection connection;

    @Getter
    private RelDataType relDataType;
    private List<RelDataType> colunmTypes;
    private List<String> columnNames;

    public MysqlTable(String schema, String tableName, Connection connection) {
        super(Object[].class);
        this.schema = schema;
        this.tableName = tableName;
        this.connection = connection;
        relToSqlConverter = new RelToSqlConverter(SqlDialect.DatabaseProduct.MYSQL.getDialect());
    }

    @Override
    public <T> Queryable<T> asQueryable(QueryProvider queryProvider, SchemaPlus schema, String tableName) {
        return new AbstractTableQueryable<T>(queryProvider, schema, this, tableName) {
            @Override
            public Enumerator<T> enumerator() {
                Iterator<Object[]> it = mysqlReader.readData();
                return (Enumerator<T>) new MyqlEnumerator(it);
            }
        };
    }

    @Override
    public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {

        final TableScan relNode = new EnumerableTableScan(context.getCluster(),
                context.getCluster().traitSetOf(EnumerableConvention.INSTANCE),
                relOptTable, (Class) getElementType());

        if (null == mysqlReader) {
            final String sql = relToSqlConverter.visit(relNode).asStatement().toString();
            mysqlReader = new MySqlReaderImpl(sql, connection, this);
        }

        return relNode;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        //get meta data from mysql;
        if (null != relDataType) {
            return relDataType;
        }

        //try to get table meta data from db;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select COLUMN_NAME, DATA_TYPE from information_schema.columns where table_schema = ? and table_name = ?");

            preparedStatement.setString(1, schema);
            preparedStatement.setString(2, tableName);
            ResultSet r = preparedStatement.executeQuery();

            columnNames = Lists.newArrayList();
            colunmTypes = Lists.newArrayList();
            while (r.next()) {
                //FIXME Ignore column case
                columnNames.add(r.getString(1).toUpperCase());
                final String columnTypeString = r.getString(2);
                final Class c = JavaTypeToSqlTypeConversion.getJavaTypeBySqlType(columnTypeString);
                colunmTypes.add(typeFactory.createJavaType(c));
            }

            relDataType = typeFactory.createStructType(colunmTypes, columnNames);
            return relDataType;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
