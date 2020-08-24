package com.yuqi.sql;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 2/8/20 17:32
 **/
public class SlothColumn {
    private String columnName;

    private EnhanceSlothColumn columnType;

    public SlothColumn(String columnName, EnhanceSlothColumn columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public EnhanceSlothColumn getColumnType() {
        return columnType;
    }

    //Others to be add;
}
