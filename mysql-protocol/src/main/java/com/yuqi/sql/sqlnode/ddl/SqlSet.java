package com.yuqi.sql.sqlnode.ddl;

import org.apache.calcite.sql.SqlDdl;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/9/20 21:14
 **/
public class SqlSet extends SqlDdl {
    public static final SqlOperator SET =
            new SqlSpecialOperator("SET", SqlKind.OTHER_DDL);
    private boolean isGobal = false;
    private String key;
    private String value;

    public SqlSet(SqlParserPos pos, boolean isGobal, String key, String value) {
        super(SET, pos);
        this.isGobal = isGobal;
        this.key = key;
        this.value = value;
    }

    public boolean isGobal() {
        return isGobal;
    }

    public void setGobal(boolean gobal) {
        isGobal = gobal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
