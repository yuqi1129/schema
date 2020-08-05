package com.yuqi.engine.data.expr;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 15:50
 **/
public class Literal extends Symbol implements Comparable<Literal> {
    private Value value;

    public Literal(Value value) {
        super(value.getType());
        this.value = value;
    }

    @Override
    public SymbolType symbolType() {
        return SymbolType.LITERAL;
    }

    @Override
    public DataType<?> valueType() {
        return value.getType();
    }

    @Override
    public int compareTo(Literal o) {
        return -1;
        //return value;
    }

    @Override
    public Value compute() {
        return value;
    }
}
