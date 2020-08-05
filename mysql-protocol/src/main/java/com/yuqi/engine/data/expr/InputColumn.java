package com.yuqi.engine.data.expr;

import com.yuqi.engine.data.value.Value;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 18:46
 **/
public class InputColumn extends Symbol {
    //this is table scan
    private Value value;

    public InputColumn(Value value) {
        super(value.getType());
        this.value = value;
    }

    @Override
    public SymbolType symbolType() {
        return SymbolType.INPUT_COLUMN;
    }

    @Override
    public Value compute() {
        return value;
    }
}
