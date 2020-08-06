package com.yuqi.engine.data.expr;

import com.yuqi.engine.data.func.Scalar;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 16:20
 **/
public class Function extends Symbol {
    private List<Symbol> args;
    private Scalar operator;


    public Function(DataType<?> returnType, List<Symbol> args, Scalar operator) {
        super(returnType);
        this.args = args;
        this.operator = operator;
    }

    @Override
    public SymbolType symbolType() {
        return SymbolType.FUNCTION;
    }

    @Override
    public Value compute() {
        final List<Value> values = args.stream()
                .map(Symbol::compute)
                .collect(Collectors.toList());

        return operator.evaluate(values, returnType);
    }

    @Override
    public void setInput(List<Value> input) {
        this.input = input;
        args.forEach(arg -> arg.setInput(input));
    }
}
