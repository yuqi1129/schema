package com.yuqi.sql.sqlnode.ddl;

import com.yuqi.protocol.enums.ShowEnum;
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
 * @time 26/7/20 22:01
 **/
public class SqlShow extends SqlDdl {

    private String command;
    private ShowEnum type;

    public String getCommand() {
        return command;
    }

    public SqlShow(SqlParserPos pos, ShowEnum type, String command) {
        super(SHOW, pos);
        this.type = type;
        this.command = command;
    }

    public static final SqlOperator SHOW =
            new SqlSpecialOperator("SHOW", SqlKind.OTHER_DDL);

    public ShowEnum getType() {
        return type;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
