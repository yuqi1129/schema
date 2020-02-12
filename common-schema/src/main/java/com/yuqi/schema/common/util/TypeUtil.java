package com.yuqi.schema.common.util;

import java.sql.ResultSet;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 8/2/20 15:58
 **/
public class TypeUtil {

    public static Object ObjectToClassData(ResultSet r, int i, Class c) {
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
}
