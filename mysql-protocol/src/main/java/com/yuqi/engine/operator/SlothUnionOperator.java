package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import org.apache.calcite.rel.type.RelDataType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.yuqi.engine.operator.SlothTableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 9/8/20 13:14
 **/
public class SlothUnionOperator extends AbstractOperator {
    private List<Operator> input;
    private boolean unionAll;

    private int i = 0;
    private int inputSize;

    private boolean hasFetchData = false;
    //union
    private Iterator<List<Value>> valueIt;

    public SlothUnionOperator(RelDataType rowTypes, List<Operator> input, boolean unionAll) {
        super(rowTypes);
        this.input = input;
        this.unionAll = unionAll;
    }

    @Override
    public void open() {
        input.forEach(Operator::open);

        inputSize = input.size();
    }

    @Override
    public List<Value> next() {
        return unionAll ? handleUnionAll() : handleNormal();
    }

    private List<Value> handleUnionAll() {
        while (i < inputSize) {
            List<Value> value = input.get(i).next();
            if (EOF != value) {
                return value;
            }
            i++;
        }

        return EOF;
    }

    private List<Value> handleNormal() {
        if (!hasFetchData) {
            List<List<Value>> valueHolder = Lists.newArrayList();
            input.forEach(o -> {
                List<Value> v;

                while ((v = o.next()) != EOF) {
                    valueHolder.add(v);
                }
            });

            valueIt = valueHolder.stream()
                    .distinct()
                    .collect(Collectors.toList())
                    .iterator();

            hasFetchData = true;
        }

        if (valueIt.hasNext()) {
            return valueIt.next();
        }

        return EOF;
    }

    @Override
    public void close() {
        input.forEach(Operator::close);

        //todo your own work;
    }
}
