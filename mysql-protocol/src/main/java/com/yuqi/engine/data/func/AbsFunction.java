package com.yuqi.engine.data.func;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.type.DataTypes;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 16:27
 **/
public class AbsFunction extends Scalar {

    //TODO 当前先做成单例，后面看看是否要用反射
    public static final AbsFunction INSTANCE = new AbsFunction();
    public AbsFunction() {
        super(1);
    }

    @Override
    public Value evaluate(List<Value> args) {
        assert args.size() == 1;
        final Value value = args.get(0);
        if (value.isNull()) {
            return value;
        }

        final Value r = value.copy();
        final DataType dataType = r.getType();

        if (DataTypes.INTEGER_TYPES.contains(dataType)) {
            r.setValue(Math.abs(value.longValue()));
        } else {
            r.setValue(Math.abs(value.doubleValue()));
        }

        return r;
    }
}
