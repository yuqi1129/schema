package com.yuqi.sql.ddl;

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
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 26/7/20 21:37
 **/
public class SqlDropDb extends SqlDdl {
    private String dbName;
    private boolean exist;

    public String getDbName() {
        return dbName;
    }

    public boolean isExist() {
        return exist;
    }

    public static final SqlOperator DROP_DATABASE =
            new SqlSpecialOperator("DROP_DATABASE", SqlKind.OTHER_DDL);

    public SqlDropDb(SqlParserPos pos, boolean exist, String dbName) {
        super(DROP_DATABASE, pos);
        this.dbName = dbName;
        this.exist = exist;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
