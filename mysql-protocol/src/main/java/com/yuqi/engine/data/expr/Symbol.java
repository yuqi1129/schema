package com.yuqi.engine.data.expr;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 15:40
 **/
public abstract class Symbol implements FuncArg {

    protected List<Value> input;

    public Symbol(DataType<?> returnType) {
        this.returnType = returnType;
    }

    public void setInput(List<Value> input) {
        this.input = input;
    }

    public List<Value> getInput() {
        return input;
    }

    protected DataType<?> returnType;

    public static boolean isLiteral(Symbol symbol, DataType<?> expectedType) {
        return symbol.symbolType() == SymbolType.LITERAL && symbol.valueType().equals(expectedType);
    }

    public abstract SymbolType symbolType();

    public abstract Value compute();

    @Override
    public boolean canBeCasted() {
        return true;
    }

    @Override
    public DataType<?> valueType() {
        return returnType;
    }
}
