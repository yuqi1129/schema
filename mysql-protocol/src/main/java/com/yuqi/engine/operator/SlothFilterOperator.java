package com.yuqi.engine.operator;

import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.rex.RexToSymbolShuttle;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import java.util.List;

import static com.yuqi.engine.operator.SlothTableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 16:15
 **/
public class SlothFilterOperator extends AbstractOperator {

    private RexNode filterCondtion;
    private Operator input;

    private Symbol filter;

    public SlothFilterOperator(RexNode filterCondtion, Operator input, RelDataType relDataType) {
        super(relDataType);
        this.filterCondtion = filterCondtion;
        this.input = input;
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
