package com.yuqi.sql;

import com.google.common.collect.Lists;
import com.yuqi.storage.constant.FileConstants;
import com.yuqi.storage.lucene.SlothTableEngine;
import org.apache.calcite.adapter.java.AbstractQueryableTable;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.List;
import java.util.Objects;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/7/20 19:55
 **/
//public class SlothTable extends AbstractQueryableTable implements TranslatableTable {
public class SlothTable extends AbstractQueryableTable {

    public static final String DEFAULT_ENGINE_NAME = "sloth";

    public static final int DEFAULT_SHARD = 1;

    private String tableName;

    private SlothSchema schema;

    private List<SlothColumn> columns;

    private RelDataType resultType;

    /**
     * Engine name, currently DEFAULT_ENGINE_NAME
     */
    private String engineName;

    /**
     * Table comment
     */
    private String tableComment;

    /**
     * Shard number
     */
    private int shardNum = DEFAULT_SHARD;

    private SlothTableEngine slothTableEngine;

    public SlothTable(String tableName, SlothSchema schema, RelDataType resultType) {
        super(Object[].class);
        this.tableName = tableName;
        this.schema = schema;
        this.resultType = resultType;
    }

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

    public SlothTable(SlothSchema slothSchema) {
        super(Object[].class);
        this.schema = slothSchema;
    }

    public SlothTableEngine getSlothTableEngine() {
        return slothTableEngine;
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

    public List<SlothColumn> getColumns() {
        return columns;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public int getShardNum() {
        return shardNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }

    public String buildTableEnginePath() {
        return FileConstants.TABLE_FILE_LOACTION + "/" + schema.getSchemaName() + "/" + tableName;
    }


    public void initTableEngine() {
        slothTableEngine = new SlothTableEngine(this);
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

        //date/time/datetime 都用long存储
        columns.forEach(cl -> {
            final SqlTypeName sqlTypeName = cl.getColumnType().getColumnType();
            RelDataType relDataType = typeFactory.createSqlType(sqlTypeName);
            relDataType = typeFactory.createTypeWithNullability(
                    relDataType, cl.getColumnType().isNullable());

            relDataTypes.add(relDataType);
            columnNames.add(cl.getColumnName());
        });

        resultType = typeFactory.createStructType(relDataTypes, columnNames);
        return resultType;
    }

  /**
   * You cant set table staticstic here
   * such as RelCollation and statatisc will be use in {@link
   * org.apache.calcite.rel.logical.LogicalTableScan#create(RelOptCluster, RelOptTable, List)}
   * @return
   */
  @Override
  public Statistic getStatistic() {
    return super.getStatistic();
  }

  //TODO 高优, 这里需要找一下原因， 没有 SlothTableScanConverterRule RULE的话，直接会报错
    // 直接toRel有问题， 在直接select * from table的情况下
//    @Override
//    public RelNode toRel(RelOptTable.ToRelContext context, RelOptTable relOptTable) {
//        return new SlothTableScan(
//                context.getCluster(),
//                RelTraitSet.createEmpty()
//                        .plus(RelCollations.EMPTY)
//                        .plus(SlothConvention.INSTANCE),
//                relOptTable);
//    }
}
