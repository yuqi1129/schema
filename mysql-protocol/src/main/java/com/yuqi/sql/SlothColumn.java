package com.yuqi.sql;

import com.yuqi.sql.sqlnode.type.SlothColumnType;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 2/8/20 17:32
 **/
public class SlothColumn {
    private String columnName;

    private SlothColumnType columnType;

    public SlothColumn(String columnName, SlothColumnType columnType1) {
        this.columnName = columnName;
        this.columnType = columnType1;
    }

    public String getColumnName() {
        return columnName;
    }

    public SlothColumnType getColumnType() {
        return columnType;
    }

    //Others to be add;
}
