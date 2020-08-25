package com.yuqi.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 24/8/20 22:43
 **/
public class StringUtil {

    public static String[] getDbAndTableName(String tableWithDb) {
        return tableWithDb.split("\\.", 2);
    }

    public static Pair<String, String> getDbAndTablePair(String tableWithDb, String dbFromConn) {
        final String[] tableAndDatabase = StringUtil.getDbAndTableName(tableWithDb);

        final String table;
        final String db;
        if (tableAndDatabase.length == 1) {
            table = tableAndDatabase[0];
            db = dbFromConn;
        } else {
            table = tableAndDatabase[0];
            db = tableAndDatabase[1];
        }

        return ImmutablePair.of(db, table);
    }
}
