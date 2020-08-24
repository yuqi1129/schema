package com.yuqi.sql.sqlnode.ddl;

import org.apache.calcite.sql.SqlDdl;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 20:05
 **/
public class SqlCreateTable extends SqlDdl {
    private String tableName;
    private SqlNodeList nameAndType;
    private boolean isNotExisted;
    private String engine;
    private SqlNode tableComment;

    public static final SqlSpecialOperator CREATE_TABLE =
            new SqlSpecialOperator("CREATE TABLE", SqlKind.OTHER_DDL);

    public SqlCreateTable(SqlParserPos pos, String tableName, SqlNodeList nameAndType,
                          boolean isNotExisted, String engine, SqlNode tableComment) {
        super(CREATE_TABLE, pos);
        this.tableName = tableName;
        this.nameAndType = nameAndType;
        this.isNotExisted = isNotExisted;

        this.engine = engine;
        this.tableComment = tableComment;
    }

    public String getTableName() {
        return tableName;
    }

    public SqlNodeList getNameAndType() {
        return nameAndType;
    }

    public boolean isNotExisted() {
        return isNotExisted;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public SqlNode getTableComment() {
        return tableComment;
    }

    public void setTableComment(SqlNode tableComment) {
        this.tableComment = tableComment;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
