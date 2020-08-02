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
    UNKNOWN_TABLE_NAME(10051, "Unknown table '%s'");


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
