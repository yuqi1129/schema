package com.yuqi.engine.operator;

import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.io.IO;

import java.util.List;
import java.util.stream.Collectors;

import static com.yuqi.engine.operator.SlothTableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:04
 **/
public class SlothProjectOperator implements Operator, IO {
    private Operator child;
    private List<Symbol> projects;


    public SlothProjectOperator(Operator child, List<Symbol> projects) {
        this.child = child;
        this.projects = projects;
    }

    @Override
    public void open() {
        child.open();

        //todo your one open logical
    }

    @Override
    public List<Value> next() {
        final List<Value> input = child.next();
        if (input == EOF) {
            return EOF;
        }

        projects.forEach(symbol -> symbol.setInput(input));
        return projects.stream().map(Symbol::compute).collect(Collectors.toList());
    }

    @Override
    public void close() {
        child.close();
    }


}
