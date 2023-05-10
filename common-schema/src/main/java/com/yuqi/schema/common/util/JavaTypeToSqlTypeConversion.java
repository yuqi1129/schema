package com.yuqi.schema.common.util;

import com.google.common.collect.ImmutableMap;
import org.apache.calcite.avatica.util.ArrayImpl;
import org.apache.calcite.sql.type.SqlTypeName;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 8/2/20 16:54
 **/
public class JavaTypeToSqlTypeConversion {

    private static final Map<Class<?>, SqlTypeName> RULES =
            ImmutableMap.<Class<?>, SqlTypeName>builder()
                    .put(Integer.class, SqlTypeName.INTEGER)
                    .put(int.class, SqlTypeName.INTEGER)
                    .put(Long.class, SqlTypeName.BIGINT)
                    .put(long.class, SqlTypeName.BIGINT)
                    .put(Short.class, SqlTypeName.SMALLINT)
                    .put(short.class, SqlTypeName.SMALLINT)
                    .put(Byte.class, SqlTypeName.TINYINT)
                    .put(byte.class, SqlTypeName.TINYINT)
                     //change from SqlTypeName.REAL to SqlTypeName.Float in float
                    .put(Float.class, SqlTypeName.FLOAT)
                    .put(float.class, SqlTypeName.FLOAT)
                    .put(Double.class, SqlTypeName.DOUBLE)
                    .put(double.class, SqlTypeName.DOUBLE)
                    .put(Boolean.class, SqlTypeName.BOOLEAN)
                    .put(boolean.class, SqlTypeName.BOOLEAN)
                    .put(byte[].class, SqlTypeName.VARBINARY)
                    .put(String.class, SqlTypeName.VARCHAR)
                    .put(char[].class, SqlTypeName.VARCHAR)
                    .put(Character.class, SqlTypeName.CHAR)
                    .put(char.class, SqlTypeName.CHAR)

                    .put(java.util.Date.class, SqlTypeName.TIMESTAMP)
                    .put(Date.class, SqlTypeName.DATE)
                    .put(Timestamp.class, SqlTypeName.TIMESTAMP)
                    .put(Time.class, SqlTypeName.TIME)
                    .put(BigDecimal.class, SqlTypeName.DECIMAL)

//                    .put(GeoFunctions.Geom.class, SqlTypeName.GEOMETRY)

                    .put(ResultSet.class, SqlTypeName.CURSOR)
                    //.put(ColumnList.class, SqlTypeName.COLUMN_LIST)
                    .put(ArrayImpl.class, SqlTypeName.ARRAY)
                    .put(List.class, SqlTypeName.ARRAY)
                    .build();

    public static Class getJavaTypeBySqlType(String sqlType) {
        //final SqlTypeName sqlTypeName =
        final String type = sqlType.toUpperCase();
        SqlTypeName sqlTypeName =
                Arrays.stream(SqlTypeName.values())
                        //FIXME sql int <--> integer
                        .filter(a -> a.name().equals(type) || a.name().toUpperCase().startsWith(type))
                        .limit(1)
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("can't find sqlType:" + sqlType));


        List<Class> r =
                RULES.entrySet().stream()
                .filter(entry -> entry.getValue() == sqlTypeName)
                .limit(1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!r.isEmpty()) {
            return r.get(0);
        }

        throw new RuntimeException("Can't find sqlType:" + sqlType);
    }
}
