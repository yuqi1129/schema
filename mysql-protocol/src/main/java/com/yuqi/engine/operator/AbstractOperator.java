package com.yuqi.engine.operator;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.io.IO;
import com.yuqi.sql.util.TypeConversionUtils;
import org.apache.calcite.rel.type.RelDataType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 8/8/20 17:08
 **/
public abstract class AbstractOperator<R> implements Operator<R>, IO {
    protected RelDataType rowTypes;

    public AbstractOperator(RelDataType rowTypes) {
        this.rowTypes = rowTypes;
    }

    @Override
    public List<DataType> getRowType() {
        return rowTypes.getFieldList().stream()
                .map(f -> f.getType().getSqlTypeName())
                .map(TypeConversionUtils::getBySqlTypeName)
                .collect(Collectors.toList());
    }
}
