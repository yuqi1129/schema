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
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 26/7/20 21:37
 **/
public class SqlDrop extends SqlDdl {
    private String name;
    private boolean exist;

    //true is drop db, false is drop table
    private boolean dropDb;

    public String getName() {
        return name;
    }

    public boolean isExist() {
        return exist;
    }

    public boolean isDropDb() {
        return dropDb;
    }

    //should split drop table and drop db;
    public static final SqlOperator DROP =
            new SqlSpecialOperator("DROP", SqlKind.OTHER_DDL);


    public SqlDrop(SqlParserPos pos, boolean exist, String name, boolean dropDb) {
        super(DROP, pos);
        this.name = name;
        this.exist = exist;
        this.dropDb = dropDb;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return null;
    }
}
