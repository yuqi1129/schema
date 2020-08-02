package com.yuqi.sql.ddl;

import org.apache.calcite.sql.SqlCreate;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 26/7/20 17:43
 **/
public class SqlCreateDb extends SqlCreate {

    public static final SqlSpecialOperator CREATE_DATABASE =
            new SqlSpecialOperator("CREATE DATABASE", SqlKind.OTHER_DDL);

    private String dbName;

    public String getDbName() {
        return dbName;
    }

    public SqlCreateDb(SqlParserPos pos,
                       boolean replace,
                       boolean ifNotExists,
                       String dbName) {
        super(CREATE_DATABASE, pos, replace, ifNotExists);
        this.dbName = dbName;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        super.unparse(writer, leftPrec, rightPrec);
    }
}
