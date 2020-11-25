package com.yuqi.engine.data.func.agg;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.type.DataTypes;
import com.yuqi.engine.data.value.Value;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 7/8/20 19:01
 **/
public class MaxAggregation extends AbstractAggregation {

    protected Value r;
    protected List<Value> v;

    public MaxAggregation(boolean isDistinct, boolean ignoreNull, DataType inputType,
                          DataType resultType, int index, List<Integer> groupByIndex) {
        super(isDistinct, ignoreNull, inputType, resultType, index, groupByIndex);
    }

    @Override
    public Value compute() {
        init();
        if (v.isEmpty()) {
            r.setValue(null);
        } else {
            Value max = v.stream().max(Value::compareTo).get();
            Value n = max.copy();
            n.setDataType(r.getType());
            r = n;
        }

        return r;
    }

    @Override
    public void init() {
        if (DataTypes.DECIMAL_TYPES.contains(inputType)) {
            r = new Value(Double.MIN_VALUE, resultType);
        } else {
            r = new Value(Long.MIN_VALUE, resultType);
        }

        v = originDatas.stream()
                .map(values -> values.getColumn(index))
                .collect(Collectors.toList());

        if (isDistinct) {
            v = v.stream().distinct().collect(Collectors.toList());
        }
    }
}
