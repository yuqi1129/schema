package com.yuqi.engine;

import com.yuqi.engine.data.Row;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 29/8/20 14:15
 **/
public class SlothRow implements Row<Value> {
    public static final SlothRow EOF_ROW = new SlothRow();

    private List<Value> rowValue;

    public SlothRow(List<Value> rowValue) {
        this.rowValue = rowValue;
    }

    public SlothRow() {
    }

    public void setRowValue(List<Value> rowValue) {
        this.rowValue = rowValue;
    }


    @Override
    public int columnSize() {

        return rowValue.size();
    }

    @Override
    public Value getColumn(int i) {
        return rowValue.get(i);
    }

    @Override
    public List<Value> getAllColumn() {
        return rowValue;
    }
}
