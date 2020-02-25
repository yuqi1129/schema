package com.yuqi.schema.common.bean;

import com.mysql.jdbc.JDBC42ResultSet;
import com.mysql.jdbc.ResultSetImpl;
import com.yuqi.schema.common.util.ReflectionUtils;
import com.yuqi.schema.common.util.TypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/2/20 17:18
 **/
@Slf4j
public class JdbcResultSetMetaDataHandler implements MetaDataHandler<JDBC42ResultSet> {


    /**
     * For JDBC result set
     */
    private static final Field JDBC42_RESULT_SET_FIELDS =
            ReflectionUtils.getField(ResultSetImpl.class, "fields");

    @Override
    public List<Class> getColumnType(JDBC42ResultSet resultSet) throws IllegalAccessException {
        final com.mysql.jdbc.Field[] columnMetaDataList =
                (com.mysql.jdbc.Field[]) JDBC42_RESULT_SET_FIELDS.get(resultSet);

        return Arrays.stream(columnMetaDataList)
                .map(com.mysql.jdbc.Field::getMysqlType)
                .map(TypeUtil::mysqlTypeToClass)
                .collect(Collectors.toList());
    }


    @Override
    public List<String> getColumnName(JDBC42ResultSet resultSet) throws IllegalAccessException {
        final com.mysql.jdbc.Field[] columnMetaDataList =
                (com.mysql.jdbc.Field[]) JDBC42_RESULT_SET_FIELDS.get(resultSet);

        return Arrays.stream(columnMetaDataList).map(f -> {
            try {
                return f.getName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}
