package com.yuqi.engine.data.expr;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/8/20 18:45
 **/
public class ColumnReference extends Symbol {
    private int index;

    public ColumnReference(DataType<?> returnType, int index) {
        super(returnType);
        this.index = index;
    }

    @Override
    public SymbolType symbolType() {
        return SymbolType.REFERNCE;
    }

    @Override
    public Value compute() {
        return getInput().get(index);
    }
}
