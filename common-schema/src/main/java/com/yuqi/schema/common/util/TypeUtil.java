package com.yuqi.schema.common.util;

import java.sql.ResultSet;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 8/2/20 15:58
 **/
public class TypeUtil {

    public static Object objectToClassData(ResultSet r, int i, Class c) {
        try {
            if (c == String.class) {
                return r.getString(i);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Class mysqlTypeToClass(int jdbcType) {

        switch (jdbcType) {
            //see MysqlDefs
            case 1:
                return Byte.class;
            case 2:
                return Short.class;
            case 3:
                return Integer.class;
            case 8:
                return Long.class;
            case 253:
            case 254:
                return String.class;
            case 4:
                return Float.class;
            case 5:
                return Double.class;
            default:
                throw new UnsupportedOperationException("do not support jdbc type: " + jdbcType);
        }
    }
}
