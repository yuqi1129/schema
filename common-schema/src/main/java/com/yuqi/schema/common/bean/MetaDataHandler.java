package com.yuqi.schema.common.bean;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/2/20 17:09
 **/
public interface MetaDataHandler<T extends ResultSet> {

    default List<Class> getColumnType(T resultSet) throws IllegalAccessException {
        return null;
    }

    default List<String> getColumnName(T resultSet) {
        return null;
    }
}
