package com.yuqi.schema.common.bean;

import com.yuqi.schema.common.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.AvaticaResultSet;
import org.apache.calcite.avatica.ColumnMetaData;
import org.apache.calcite.jdbc.CalciteResultSet;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/2/20 17:12
 **/
@Slf4j
public class CalciteResultSetMetaDataHandler implements MetaDataHandler<CalciteResultSet>  {

    /**
     * For calcite result set
     */
    private static final Field CALCITE_RESULT_SET_COLUMN_METADATA_LIST =
            ReflectionUtils.getField(AvaticaResultSet.class, "columnMetaDataList");

    @Override
    public List<Class> getColumnType(CalciteResultSet resultSet) throws IllegalAccessException {
        final List<ColumnMetaData> columnMetaDataList = (List<ColumnMetaData>) CALCITE_RESULT_SET_COLUMN_METADATA_LIST.get(resultSet);
        return columnMetaDataList.stream().map(a -> a.type.rep.clazz).collect(Collectors.toList());
    }

    @Override
    public List<String> getColumnName(CalciteResultSet resultSet) throws IllegalAccessException {
        final List<ColumnMetaData> columnMetaDataList = (List<ColumnMetaData>) CALCITE_RESULT_SET_COLUMN_METADATA_LIST.get(resultSet);
        return columnMetaDataList.stream().map(a -> a.label).collect(Collectors.toList());
    }
}
