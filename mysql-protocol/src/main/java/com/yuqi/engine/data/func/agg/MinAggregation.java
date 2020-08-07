package com.yuqi.engine.data.func.agg;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.type.DataTypes;
import com.yuqi.engine.data.value.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 7/8/20 19:16
 **/
public class MinAggregation extends MaxAggregation {

    public MinAggregation(boolean isDistinct, boolean ignoreNull, DataType inputType,
                          DataType resultType, int index, List<Integer> groupByIndex) {
        super(isDistinct, ignoreNull, inputType, resultType, index, groupByIndex);
    }

    @Override
    public Value compute() {
        init();
        if (v.isEmpty()) {
            r.setValue(null);
        } else {
            Value min = v.stream().min(Value::compareTo).get();
            Value n = min.copy();
            n.setDataType(r.getType());
            r = n;
        }

        return r;
    }

    @Override
    public void init() {
        if (DataTypes.DECIMAL_TYPES.contains(inputType)) {
            r = new Value(Double.MAX_VALUE, resultType);
        } else {
            r = new Value(Long.MAX_VALUE, resultType);
        }

        v = originDatas.stream()
                .map(values -> values.get(index))
                .collect(Collectors.toList());

        if (isDistinct) {
            v = v.stream().distinct().collect(Collectors.toList());
        }
    }
}
