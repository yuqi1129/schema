package com.yuqi.protocol.constants;

import com.google.common.collect.Maps;
import org.apache.calcite.sql.type.SqlTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 6/7/20 23:26
 **/
public class ColumnTypeConstants {

    public static final Logger LOGGER = LoggerFactory.getLogger(ColumnTypeConstants.class);

    public static final int MYSQL_TYPE_DECIMAL = 0x00;
    public static final int MYSQL_TYPE_TINY = 0x01;
    public static final int MYSQL_TYPE_SHORT = 0x02;
    public static final int MYSQL_TYPE_LONG = 0x03;
    public static final int MYSQL_TYPE_FLOAT = 0x04;
    public static final int MYSQL_TYPE_DOUBLE = 0x05;

    public static final int MYSQL_TYPE_NULL = 0x06;

    public static final int MYSQL_TYPE_TIMESTAMP = 0x07;
    public static final int MYSQL_TYPE_LONGLONG = 0x08;
    public static final int MYSQL_TYPE_INT24 = 0x09;


    public static final int MYSQL_TYPE_DATE = 0x0a;
    public static final int MYSQL_TYPE_TIME = 0x0b;
    public static final int MYSQL_TYPE_DATETIME = 0x0c;
    public static final int MYSQL_TYPE_YEAR = 0x0d;
    public static final int MYSQL_TYPE_NEWDATE = 0x0e;

    public static final int MYSQL_TYPE_VARCHAR = 0x0f;
    public static final int MYSQL_TYPE_BIT = 0x10;
    public static final int MYSQL_TYPE_TIMESTAMP2 = 0x11;
    public static final int MYSQL_TYPE_DATETIME2 = 0x12;
    public static final int MYSQL_TYPE_TIME2 = 0x13;

    public static final int MYSQL_TYPE_NEWDECIMAL = 0xf6;
    public static final int MYSQL_TYPE_ENUM = 0xf7;

    public static final int MYSQL_TYPE_SET = 0xf8;
    public static final int MYSQL_TYPE_TINY_BLOB = 0xf9;
    public static final int MYSQL_TYPE_MEDIUM_BLOB = 0xfa;
    public static final int MYSQL_TYPE_LONG_BLOB = 0xfb;
    public static final int MYSQL_TYPE_BLOB = 0xfc;

    public static final int MYSQL_TYPE_VAR_STRING = 0xfd;
    public static final int MYSQL_TYPE_STRING = 0xfe;
    public static final int MYSQL_TYPE_GEOMETRY = 0xff;


    public static final Map<SqlTypeName, Integer> CALCITE_TYPE_TO_MYSQL_TYPE = Maps.newHashMap();

    static {
        //MySQL 使用tinyint(1) 来表示boolean type
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.BOOLEAN, MYSQL_TYPE_TINY);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.TINYINT, MYSQL_TYPE_TINY);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.SMALLINT, MYSQL_TYPE_SHORT);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.INTEGER, MYSQL_TYPE_INT24);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.BIGINT, MYSQL_TYPE_LONG);


        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.FLOAT, MYSQL_TYPE_FLOAT);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.DOUBLE, MYSQL_TYPE_DOUBLE);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.DECIMAL, MYSQL_TYPE_DECIMAL);

        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.DATE, MYSQL_TYPE_DATE);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.TIME, MYSQL_TYPE_TIME);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.TIMESTAMP, MYSQL_TYPE_TIMESTAMP);

        //only support varchar
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.VARCHAR, MYSQL_TYPE_VAR_STRING);
        CALCITE_TYPE_TO_MYSQL_TYPE.put(SqlTypeName.CHAR, MYSQL_TYPE_VAR_STRING);
    }

    public static final int getMysqlType(SqlTypeName sqlTypeName) {
        Integer type = CALCITE_TYPE_TO_MYSQL_TYPE.get(sqlTypeName);

        if (Objects.isNull(type)) {
            LOGGER.info("SqlTypeName {} has no correspond mysql type, use MYSQL_TYPE_VAR_STRING(0xfd)", sqlTypeName);
            type = MYSQL_TYPE_VAR_STRING;
        }

        return type;
    }


}
