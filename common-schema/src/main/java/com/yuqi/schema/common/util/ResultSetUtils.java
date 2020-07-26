package com.yuqi.schema.common.util;

import com.google.common.collect.Maps;
import com.mysql.jdbc.JDBC42ResultSet;
import com.yuqi.schema.common.bean.CalciteResultSetMetaDataHandler;
import com.yuqi.schema.common.bean.DerbyResultSetMetaDataHandler;
import com.yuqi.schema.common.bean.JdbcResultSetMetaDataHandler;
import com.yuqi.schema.common.bean.MetaDataHandler;
import org.apache.calcite.jdbc.CalciteResultSet;
import org.apache.derby.impl.jdbc.EmbedResultSet42;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 18/2/20 22:17
 **/
public class ResultSetUtils {

    private static final Map<Class, MetaDataHandler> META_DATA_HANDLER_MAP
            = Maps.newHashMap();

    static {
        META_DATA_HANDLER_MAP.put(CalciteResultSet.class, new CalciteResultSetMetaDataHandler());
        META_DATA_HANDLER_MAP.put(JDBC42ResultSet.class, new JdbcResultSetMetaDataHandler());
        META_DATA_HANDLER_MAP.put(EmbedResultSet42.class, new DerbyResultSetMetaDataHandler());
    }

    public static List<Class> getColumnTypeFromResultSet(ResultSet resultSet) throws IllegalAccessException {

        final MetaDataHandler<ResultSet> handler = META_DATA_HANDLER_MAP.get(resultSet.getClass());
        //unsupport yet
        if (null == handler) {
            throw new UnsupportedOperationException("Do not support " + resultSet.getClass().getSimpleName() + " now...");
        }

        return handler.getColumnType(resultSet);
    }

    public static List<String> getColumnNameFromResultSet(ResultSet resultSet) throws IllegalAccessException {
        final MetaDataHandler<ResultSet> handler = META_DATA_HANDLER_MAP.get(resultSet.getClass());
        //unsupport yet
        if (null == handler) {
            throw new UnsupportedOperationException("Do not support " + resultSet.getClass().getSimpleName() + " now...");
        }

        return handler.getColumnName(resultSet);
    }

    public static String javaTypeToString(ResultSet rs, int index, Class<?> clzz) throws SQLException {

        final String result = rs.getString(index);
        if (null == result) {
            return null;
        }

        if (clzz == Byte.class) {
            return String.valueOf(rs.getByte(index));
        } else if (clzz == Short.class) {
            return String.valueOf(rs.getShort(index));
        } else if (clzz == Integer.class) {
            return String.valueOf(rs.getInt(index));
        } else if (clzz == Long.class) {
            return String.valueOf(rs.getLong(index));
        } else if (clzz == String.class) {
            return result;
        } else if (clzz == Float.class) {
            return String.valueOf(rs.getFloat(index));
        } else if (clzz == Double.class) {
            return String.valueOf(rs.getDouble(index));
        }

        return result;
    }

    public static Object javaTypeToObject(ResultSet rs, int index, Class<?> clzz) throws SQLException {

        final String result = rs.getString(index);
        if (null == result) {
            return null;
        }

        if (clzz == Byte.class) {
            return rs.getByte(index);
        } else if (clzz == Short.class) {
            return rs.getShort(index);
        } else if (clzz == Integer.class) {
            return rs.getInt(index);
        } else if (clzz == Long.class) {
            return rs.getLong(index);
        } else if (clzz == String.class) {
            return result;
        } else if (clzz == Float.class) {
            return rs.getFloat(index);
        } else if (clzz == Double.class) {
            return rs.getDouble(index);
        }

        return result;
    }
}
