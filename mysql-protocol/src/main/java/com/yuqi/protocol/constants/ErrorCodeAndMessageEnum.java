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
    TABLE_ALREADY_EXISTS(1050, "Table '%' already exists"),

    /**
     * database exists when create database exists
     */
    DATABASE_EXISTS_ERROR(1007, "Can't create database '%s'; database exists");



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
