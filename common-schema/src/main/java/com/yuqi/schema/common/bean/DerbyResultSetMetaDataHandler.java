package com.yuqi.schema.common.bean;

import com.yuqi.schema.common.util.JavaTypeToSqlTypeConversion;
import com.yuqi.schema.common.util.ReflectionUtils;
import org.apache.derby.impl.jdbc.EmbedResultSet;
import org.apache.derby.impl.jdbc.EmbedResultSet42;
import org.apache.derby.impl.sql.GenericResultDescription;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/2/20 17:24
 **/
public class DerbyResultSetMetaDataHandler implements MetaDataHandler<EmbedResultSet42> {

    /**
     * For derby result set
     */
    private static final Field EMBED_RESULT_SET;

    static {
        EMBED_RESULT_SET = ReflectionUtils.getField(EmbedResultSet.class, "resultDescription");
    }

    @Override
    public List<Class> getColumnType(EmbedResultSet42 resultSet) throws IllegalAccessException {
        final GenericResultDescription resultDesc = (GenericResultDescription) EMBED_RESULT_SET.get(resultSet);
        return Arrays.stream(resultDesc.getColumnInfo())
                .map(d -> d.getType().getTypeId().getSQLTypeName())
                .map(JavaTypeToSqlTypeConversion::getJavaTypeBySqlType)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getColumnName(EmbedResultSet42 resultSet) {
        return Collections.emptyList();
    }
}
