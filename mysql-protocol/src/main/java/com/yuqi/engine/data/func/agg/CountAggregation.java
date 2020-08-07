package com.yuqi.engine.data.func.agg;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 7/8/20 16:34
 **/
public class CountAggregation extends AbstractAggregation {
    private Value result;
    private long count;
    private boolean countStart;

    private List<Value> v;

    public CountAggregation(boolean isDistinct, boolean ignoreNull, DataType inputType, DataType resultType,
                            int index, boolean countStart) {
        super(isDistinct, ignoreNull, inputType, resultType, index);
        this.countStart = countStart;
    }

    @Override
    public void init() {
        result = new Value(0L, getResultType());
        count = 0;

        v = originDatas.stream()
                .map(values -> countStart ? values.get(0) : values.get(index))
                .collect(Collectors.toList());

        if (!countStart && isDistinct) {
            v = v.stream().distinct().collect(Collectors.toList());
        }
    }

    @Override
    public Value compute() {

        init();
        for (Value value : v) {
            if (countStart) {
                count++;
            } else {
                if (!value.isNull() || (value.isNull() && !ignoreNull)) {
                    count++;
                }
            }
        }
        result.setValue(count);
        return result;
    }
}
