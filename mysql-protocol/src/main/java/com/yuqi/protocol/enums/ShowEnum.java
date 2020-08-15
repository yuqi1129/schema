package com.yuqi.protocol.enums;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/7/20 23:41
 **/
public enum ShowEnum {

    /**
     * show tables;
     */
    SHOW_TABLBS(1, "tables"),

    /**
     * show databases;
     */
    SHOW_DBS(2, "databases"),

    /**
     * show create table
     */
    SHOW_CREATE(3, "create");

    private final int index;
    private final String startKeyWord;

    ShowEnum(int index, String startKeyWord) {
        this.index = index;
        this.startKeyWord = startKeyWord;
    }
}
