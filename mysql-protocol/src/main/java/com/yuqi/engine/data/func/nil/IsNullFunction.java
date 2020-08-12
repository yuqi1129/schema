package com.yuqi.engine.data.func.nil;

import com.yuqi.engine.data.func.Scalar;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;

import static com.yuqi.engine.data.type.DataTypes.BOOLEAN;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 12/8/20 15:19
 **/
public class IsNullFunction extends Scalar {

    public static final IsNullFunction INSTANCE = new IsNullFunction();

    public IsNullFunction() {
        super(1);
    }

    @Override
    public Value evaluate(List<Value> args, DataType returnType) {
        final Value data = args.get(0);
        return new Value(data.isNull(), BOOLEAN);
    }
}

