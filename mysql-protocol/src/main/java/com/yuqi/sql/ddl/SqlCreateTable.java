package com.yuqi.sql.ddl;

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
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 20:05
 **/
public class SqlCreateTable extends SqlDdl {
    private String tableName;
    private SqlNodeList nameAndType;
    private boolean isNotExisted;

    public static final SqlSpecialOperator CREATE_TABLE =
            new SqlSpecialOperator("CREATE TABLE", SqlKind.OTHER_DDL);

    public SqlCreateTable(SqlParserPos pos, String tableName, SqlNodeList nameAndType, boolean isNotExisted) {
        super(CREATE_TABLE, pos);
        this.tableName = tableName;
        this.nameAndType = nameAndType;
        this.isNotExisted = isNotExisted;
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

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
