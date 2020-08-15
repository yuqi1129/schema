package com.yuqi.protocol.constants;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 2/8/20 16:27
 **/
public enum ErrorCodeAndMessageEnum {

    /**
     * User or password is wrong
     */
    PASSWORD_OR_USER_IS_WRONG(1, "Wrong username or password"),

    /**
     * drop database xxx and xxx does not exsits
     */
    DATABASE_NOT_EXIST_IN_DROP(1007, "Can't drop database '%s'; database doesn't exist"),

    /**
     * database exists when create database exists
     */
    DATABASE_EXISTS_ERROR(1007, "Can't create database '%s'; database exists"),

    /**
     * When create table, no db select
     */
    NO_DATABASE_SELECTED(1046, "No database selected"),


    /**
     * When create table
     *
     * create tabel `db.t` and db does not exist
     */
    UNKNOWN_DB_NAME(1049, "Unknown database '%s'"),


    /**
     * when create table t and t has already existed
     */
    TABLE_ALREADY_EXISTS(1050, "Table '%s' already exists"),

    /**
     * Drop table 'yuqi.test', if yuqi or test does not exists
     */
    UNKNOWN_TABLE_NAME(1051, "Unknown table '%s'"),

    /**
     * Unknwn column name
     */
    UNKONW_COLUMN_NAME(1054, "Unknown column '%s' in 'field list'"),

    /**
     * Do not support this syntax
     */
    SYNTAX_ERROR(1064,
            "You have an error in your SQL syntax; check the manual"
                    + " that corresponds to your MySQL server version for the right syntax to use near '%s'"),

    /**
     * insert into t(c1, c2) value(....)
     */
    COLUMN_EXIST_TWICE(1110, "Column '%s' specified twice"),

    /**
     * insert into student(h,t) values(1); will encounter this problem
     */
    COLUMN_COUNT_NOT_MATCH(1136, "Column count doesn't match value count at row %s"),

    /**
     * Table do not exist
     */
    TABLE_NOT_EXISTS(1146, "Table '%s' doesn't exist");


    private final int code;
    private final String message;

    ErrorCodeAndMessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
