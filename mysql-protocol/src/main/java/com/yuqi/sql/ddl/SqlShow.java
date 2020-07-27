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
 * @time 26/7/20 22:01
 **/
public class SqlShow extends SqlDdl {
    private String command;

    public String getCommand() {
        return command;
    }

    public static final SqlOperator SHOW =
            new SqlSpecialOperator("SHOW", SqlKind.OTHER_DDL);


    public SqlShow(SqlParserPos pos, String command) {
        super(SHOW, pos);
        this.command = command;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
