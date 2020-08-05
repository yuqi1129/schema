package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.io.IO;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.type.RelDataType;

import java.util.Iterator;
import java.util.List;

import static com.yuqi.engine.data.type.DataTypes.LONG;
import static com.yuqi.engine.data.type.DataTypes.STRING;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 15:46
 **/
public class SlothTableScanOperator implements Operator, IO {
    public static final List<Value> EOF = null;

    private RelOptTable table;
    private RelDataType rowType;

    //MOCK
    private Iterator<List<Value>> iterator;

    public SlothTableScanOperator(RelOptTable table, RelDataType rowType) {
        this.table = table;
        this.rowType = rowType;
    }

    @Override
    public void open() {

        //TODO currently data is mock
        List<List<Value>> valuesLists = Lists.newArrayList();
        List<Value> v1 = Lists.newArrayList(new Value(1L, LONG), new Value("hello", STRING));
        List<Value> v2 = Lists.newArrayList(new Value(2L, LONG), new Value("good", STRING));
        List<Value> v3 = Lists.newArrayList(new Value(3L, LONG), new Value("nice", STRING));

        valuesLists.add(v1);
        valuesLists.add(v2);
        valuesLists.add(v3);

        iterator = valuesLists.iterator();
    }

    @Override
    public List<Value> next() {
        while (iterator.hasNext()) {
            return iterator.next();
        }

        return EOF;
    }

    @Override
    public void close() {

    }
}
