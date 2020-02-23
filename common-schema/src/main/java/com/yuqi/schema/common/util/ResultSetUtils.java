package com.yuqi.schema.common.util;

import com.mysql.jdbc.JDBC42ResultSet;
import com.mysql.jdbc.ResultSetImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.AvaticaResultSet;
import org.apache.calcite.avatica.ColumnMetaData;
import org.apache.calcite.jdbc.CalciteResultSet;
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
@Slf4j
public class ResultSetUtils {

    /**
     * For calcite result set
     */
    private static final Field CALCITE_RESULT_SET_COLUMN_METADATA_LIST;

    /**
     * For JDBC result set
     */
    private static final Field JDBC42_RESULT_SET_FIELDS;

    /**
     * For derby result set
     */
    private static final Field EMBED_RESULT_SET42;

    static {
       try {
           CALCITE_RESULT_SET_COLUMN_METADATA_LIST = AvaticaResultSet.class.getDeclaredField("columnMetaDataList");
           CALCITE_RESULT_SET_COLUMN_METADATA_LIST.setAccessible(true);

           JDBC42_RESULT_SET_FIELDS = ResultSetImpl.class.getDeclaredField("fields");
           JDBC42_RESULT_SET_FIELDS.setAccessible(true);

           EMBED_RESULT_SET42 = EmbedResultSet.class.getDeclaredField("resultDescription");
           EMBED_RESULT_SET42.setAccessible(true);
       } catch (Exception e) {
           log.error("init refection get error:" + e.getMessage());
           throw new RuntimeException(e.getMessage());
       }
    }

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

    private static List<Class> handleCalciteResultSet(CalciteResultSet calciteResultSet) throws IllegalAccessException {
        final List<ColumnMetaData> columnMetaDataList = (List<ColumnMetaData>) CALCITE_RESULT_SET_COLUMN_METADATA_LIST.get(calciteResultSet);
        return columnMetaDataList.stream().map(a -> a.type.rep.clazz).collect(Collectors.toList());
    }

    private static List<Class> handleMysqlResultSet(JDBC42ResultSet rs) throws IllegalAccessException {
        final com.mysql.jdbc.Field[] columnMetaDataList = (com.mysql.jdbc.Field[]) JDBC42_RESULT_SET_FIELDS.get(rs);

        return Arrays.stream(columnMetaDataList)
                .map(com.mysql.jdbc.Field::getMysqlType)
                .map(TypeUtil::mysqlTypeToClass)
                .collect(Collectors.toList());
    }

    private static List<Class> handleEmbedResultSet(EmbedResultSet42 rs) throws NoSuchFieldException, IllegalAccessException {
        final GenericResultDescription resultDesc = (GenericResultDescription) EMBED_RESULT_SET42.get(rs);
        return Arrays.stream(resultDesc.getColumnInfo())
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
