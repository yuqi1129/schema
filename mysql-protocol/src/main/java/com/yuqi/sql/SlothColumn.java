package com.yuqi.sql;

import org.apache.calcite.sql.SqlDataTypeSpec;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 2/8/20 17:32
 **/
public class SlothColumn {
    private String columnName;

    private SqlDataTypeSpec columnType;

    public SlothColumn(String columnName, SqlDataTypeSpec columnType1) {
        this.columnName = columnName;
        this.columnType = columnType1;
    }

    public String getColumnName() {
        return columnName;
    }

    public SqlDataTypeSpec getColumnType() {
        return columnType;
    }

    //Others to be add;
}
