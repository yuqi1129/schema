package com.yuqi.sql.ddl;

import org.apache.calcite.sql.SqlDdl;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSpecialOperator;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 20:05
 **/
public class SqlCreateTable extends SqlDdl {
    private String tableName;
    private Map<String, String> nameAndType;
    private boolean isNotExisted;

    public static final SqlSpecialOperator CREATE_TABLE =
            new SqlSpecialOperator("CREATE TABLE", SqlKind.OTHER_DDL);

    public SqlCreateTable(SqlParserPos pos, String tableName, Map<String, String> nameAndType, boolean isNotExisted) {
        super(CREATE_TABLE, pos);
        this.tableName = tableName;
        this.nameAndType = nameAndType;
        this.isNotExisted = isNotExisted;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
