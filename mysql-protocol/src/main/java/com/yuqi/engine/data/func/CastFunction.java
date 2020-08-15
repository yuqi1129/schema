package com.yuqi.engine.data.func;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 9/8/20 18:22
 **/
public class CastFunction extends Scalar {

    public static final CastFunction INSTANCE = new CastFunction();

    public CastFunction() {
        super(1);
    }

    @Override
    public Value evaluate(List<Value> args, DataType returnType) {

        final Value v = args.get(0);
        final Value copy = v.copy();
        copy.setDataType(returnType);

        return copy;
    }
}
