package com.yuqi.engine.operator;

import com.yuqi.engine.SlothRow;
import com.yuqi.engine.data.expr.Symbol;
import org.apache.calcite.rel.type.RelDataType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 16:04
 **/
public class SlothProjectOperator extends AbstractOperator<SlothRow> {
    private Operator<SlothRow> child;
    private List<Symbol> projects;

    public SlothProjectOperator(Operator<SlothRow> child, List<Symbol> projects, RelDataType rowType) {
        super(rowType);
        this.child = child;
        this.projects = projects;
    }

    @Override
    public void open() {
        child.open();

        //todo your one open logical
    }

    @Override
    public SlothRow next() {
        final SlothRow input = child.next();
        if (input == SlothRow.EOF_ROW) {
            return SlothRow.EOF_ROW;
        }

        projects.forEach(symbol -> symbol.setInput(input.getAllColumn()));
        return new SlothRow(projects.stream().map(Symbol::compute).collect(Collectors.toList()));
    }

    @Override
    public void close() {
        child.close();
    }


}
