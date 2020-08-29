package com.yuqi.engine.operator;

import com.yuqi.engine.SlothRow;
import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.sql.rex.RexToSymbolShuttle;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 16:15
 **/
public class SlothFilterOperator extends AbstractOperator<SlothRow> {

    private RexNode filterCondtion;
    private Operator<SlothRow> input;

    private Symbol filter;

    public SlothFilterOperator(RexNode filterCondtion, Operator<SlothRow> input, RelDataType relDataType) {
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
    public SlothRow next() {
        SlothRow r;

        while ((r = input.next()) != SlothRow.EOF_ROW) {
            filter.setInput(r.getAllColumn());
            if (filter.compute().booleanValue()) {
                return r;
            }
        }

        return SlothRow.EOF_ROW;
    }

    @Override
    public void close() {
        input.close();
    }
}
