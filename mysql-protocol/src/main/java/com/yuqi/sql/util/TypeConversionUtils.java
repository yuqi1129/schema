package com.yuqi.sql.util;

import com.google.common.collect.Maps;
import com.yuqi.engine.data.type.DataType;
import org.apache.calcite.sql.type.SqlTypeName;

import java.util.Map;
import java.util.Objects;

import static com.yuqi.engine.data.type.DataTypes.BOOLEAN;
import static com.yuqi.engine.data.type.DataTypes.BYTE;
import static com.yuqi.engine.data.type.DataTypes.DOUBLE;
import static com.yuqi.engine.data.type.DataTypes.FLOAT;
import static com.yuqi.engine.data.type.DataTypes.INTEGER;
import static com.yuqi.engine.data.type.DataTypes.LONG;
import static com.yuqi.engine.data.type.DataTypes.SHORT;
import static com.yuqi.engine.data.type.DataTypes.STRING;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 19:17
 **/
public class TypeConversionUtils {

    public static final Map<SqlTypeName, DataType> SQL_TYPE_TO_DATA_TYPE = Maps.newHashMap();


    static {
        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.INTEGER, INTEGER);
        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.SMALLINT, SHORT);
        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.TINYINT, BYTE);
        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.BIGINT, LONG);

        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.FLOAT, FLOAT);

        //Treat Decimal as double
        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.DECIMAL, DOUBLE);
        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.DOUBLE, DOUBLE);

        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.BOOLEAN, BOOLEAN);

        SQL_TYPE_TO_DATA_TYPE.put(SqlTypeName.VARCHAR, STRING);
    }

    public static DataType getBySqlTypeName(SqlTypeName sqlTypeName) {
        DataType r = SQL_TYPE_TO_DATA_TYPE.get(sqlTypeName);
        if (Objects.isNull(r)) {
            throw new UnsupportedOperationException("Currently we do not support type: " + sqlTypeName);
        }

        return r;
    }
}
