package com.yuqi.sql;

import org.apache.calcite.sql.type.SqlTypeName;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/8/20 19:29
 **/
public class EnhanceSlothColumn {

    /**
     * Column name
     */
    private String columName;

    /**
     * Column type
     */
    private SqlTypeName columnType;

    /**
     * True is unsigned, false not
     *
     * Attention, Currently java cant implement unsigned syntax
     */
    private boolean unsigned;

    /**
     * Whether the column can be null
     */
    private boolean nullable;

    /**
     * All kind of default value set string
     */
    private String defalutValue;

    /**
     * String column comment
     */
    private String columnComment;

    /**
     * Not used now
     */
    private int precision;

    public String getColumName() {
        return columName;
    }

    public void setColumName(String columName) {
        this.columName = columName;
    }

    public SqlTypeName getColumnType() {
        return columnType;
    }

    public void setColumnType(SqlTypeName columnType) {
        this.columnType = columnType;
    }

    public boolean isUnsigned() {
        return unsigned;
    }

    public void setUnsigned(boolean unsigned) {
        this.unsigned = unsigned;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDefalutValue() {
        return defalutValue;
    }

    public void setDefalutValue(String defalutValue) {
        this.defalutValue = defalutValue;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
