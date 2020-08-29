package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.SlothRow;
import org.apache.calcite.rel.type.RelDataType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 9/8/20 13:14
 **/
public class SlothUnionOperator extends AbstractOperator<SlothRow> {
    private List<Operator<SlothRow>> input;
    private boolean unionAll;

    private int i = 0;
    private int inputSize;

    private boolean hasFetchData = false;
    //union
    private Iterator<SlothRow> valueIt;

    public SlothUnionOperator(RelDataType rowTypes, List<Operator<SlothRow>> input, boolean unionAll) {
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
    public SlothRow next() {
        return unionAll ? handleUnionAll() : handleNormal();
    }

    private SlothRow handleUnionAll() {
        while (i < inputSize) {
            SlothRow value = input.get(i).next();
            if (SlothRow.EOF_ROW != value) {
                return value;
            }
            i++;
        }

        return SlothRow.EOF_ROW;
    }

    private SlothRow handleNormal() {
        if (!hasFetchData) {
            List<SlothRow> valueHolder = Lists.newArrayList();
            input.forEach(o -> {
                SlothRow v;

                while ((v = o.next()) != SlothRow.EOF_ROW) {
                    valueHolder.add(v);
                }
            });

            //也可以直接是Set去重
            valueIt = valueHolder.stream()
                    .distinct()
                    .collect(Collectors.toList())
                    .iterator();

            hasFetchData = true;
        }

        if (valueIt.hasNext()) {
            return valueIt.next();
        }

        return SlothRow.EOF_ROW;
    }

    @Override
    public void close() {
        input.forEach(Operator::close);

        //todo your own work;
    }
}
