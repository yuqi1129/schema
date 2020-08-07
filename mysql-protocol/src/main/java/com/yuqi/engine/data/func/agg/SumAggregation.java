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
 * @time 7/8/20 17:03
 **/
public class SumAggregation extends AbstractAggregation {

    private Value r;
    private List<Value> v;

    public SumAggregation(boolean isDistinct, boolean ignoreNull, DataType inputType, DataType resultType, int index) {
        super(isDistinct, ignoreNull, inputType, resultType, index);
    }

    @Override
    public Value compute() {
        init();
        if (v.isEmpty()) {
            r.setValue(null);
            return r;
        }

        if (DataTypes.DECIMAL_TYPES.contains(inputType)) {
            Double t = 0.0;
            for (Value value : v) {
                if (!value.isNull()) {
                    t += (Double) value.getValueByType();
                }
            }

            r.setValue(t);
        } else {
            Long t = 0L;
            for (Value value : v) {
                if (!value.isNull()) {
                    t += (Long) value.getValueByType();
                }
            }

            r.setValue(t);
        }

        return r;
    }

    @Override
    public void init() {
        if (DataTypes.DECIMAL_TYPES.contains(inputType)) {
            r = new Value(0.0, resultType);
        } else {
            r = new Value(0L, resultType);
        }

        v = originDatas.stream()
                .map(values -> values.get(index))
                .collect(Collectors.toList());

        if (isDistinct) {
            v = v.stream().distinct().collect(Collectors.toList());
        }
    }
}
