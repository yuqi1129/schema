package com.yuqi.engine.operator;

import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.io.IO;
import com.yuqi.sql.rex.RexToSymbolShuttle;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import java.util.List;

import static com.yuqi.engine.operator.SlothTableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:15
 **/
public class SlothFilterOperator implements Operator, IO {

    private RexNode filterCondtion;
    private Operator input;
    private RelDataType relDataType;


    private Symbol filter;

    public SlothFilterOperator(RexNode filterCondtion, Operator input, RelDataType relDataType) {
        this.filterCondtion = filterCondtion;
        this.input = input;
        this.relDataType = relDataType;
    }

    @Override
    public void open() {
        input.open();
        filter = filterCondtion.accept(RexToSymbolShuttle.INSTANCE);
    }

    @Override
    public List<Value> next() {
        List<Value> r;

        while ((r = input.next()) != EOF) {
            filter.setInput(r);
            if (filter.compute().booleanValue()) {
                return r;
            }
        }

        return EOF;
    }

    @Override
    public void close() {
        input.close();
    }
}
