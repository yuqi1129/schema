package com.yuqi.schema.common.util;

import com.mysql.jdbc.JDBC42ResultSet;
import com.mysql.jdbc.ResultSetImpl;
import org.apache.calcite.avatica.AvaticaResultSet;
import org.apache.calcite.avatica.ColumnMetaData;
import org.apache.calcite.jdbc.CalciteResultSet;
import org.apache.derby.iapi.sql.ResultColumnDescriptor;
import org.apache.derby.impl.jdbc.EmbedResultSet;
import org.apache.derby.impl.jdbc.EmbedResultSet42;
import org.apache.derby.impl.sql.GenericResultDescription;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 18/2/20 22:17
 **/
public class ResultSetUtils {

    public static List<Class> getClassFromResultSet(ResultSet resultSet) throws NoSuchFieldException, IllegalAccessException {
        if (resultSet instanceof CalciteResultSet) {
            return handleCalciteResultSet((CalciteResultSet) resultSet);
        } else if (resultSet instanceof JDBC42ResultSet) {
            return handleMysqlResultSet((JDBC42ResultSet) resultSet);
        } else if (resultSet instanceof EmbedResultSet42) {
            return handleEmbedResultSet((EmbedResultSet42) resultSet);
        }

        throw new UnsupportedOperationException("Do not support " + resultSet.getClass().getSimpleName() + " now...");
    }

    private static List<Class> handleCalciteResultSet(CalciteResultSet calciteResultSet) throws NoSuchFieldException, IllegalAccessException {

        final Field f = AvaticaResultSet.class.getDeclaredField("columnMetaDataList");
        f.setAccessible(true);
        final List<ColumnMetaData> columnMetaDataList = (List<ColumnMetaData>) f.get(calciteResultSet);
        return columnMetaDataList.stream().map(a -> a.type.rep.clazz).collect(Collectors.toList());
    }

    //
    private static List<Class> handleMysqlResultSet(JDBC42ResultSet rs) throws IllegalAccessException, NoSuchFieldException {
        final Field f = ResultSetImpl.class.getDeclaredField("fields");
        f.setAccessible(true);

        final com.mysql.jdbc.Field[] columnMetaDataList = (com.mysql.jdbc.Field[]) f.get(rs);

        return Arrays.stream(columnMetaDataList)
                .map(t -> t.getMysqlType())
                .map(TypeUtil::mysqlTypeToClass)
                .collect(Collectors.toList());
    }


    private static List<Class> handleEmbedResultSet(EmbedResultSet42 rs) throws NoSuchFieldException, IllegalAccessException {

        final Field f = EmbedResultSet.class.getDeclaredField("resultDescription");
        f.setAccessible(true);
        final GenericResultDescription resultDesc = (GenericResultDescription) f.get(rs);

        final ResultColumnDescriptor[] descriptors = resultDesc.getColumnInfo();

        return Arrays.stream(descriptors)
                .map(d -> d.getType().getTypeId().getSQLTypeName())
                .map(JavaTypeToSqlTypeConversion::getJavaTypeBySqlType)
                .collect(Collectors.toList());
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
