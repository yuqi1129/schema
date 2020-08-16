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
 * @time 31/7/20 15:03
 **/
public class SqlUse extends SqlDdl {
    private String db;

    public static final SqlOperator SHOW =
            new SqlSpecialOperator("USE", SqlKind.OTHER_DDL);

    public SqlUse(SqlParserPos pos, String db) {
        super(SHOW, pos);
        this.db = db;
    }

    public String getDb() {
        return db;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
