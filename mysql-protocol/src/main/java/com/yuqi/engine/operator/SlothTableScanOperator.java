package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.rel.type.RelDataType;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.yuqi.engine.data.type.DataTypes.DOUBLE;
import static com.yuqi.engine.data.type.DataTypes.LONG;
import static com.yuqi.engine.data.type.DataTypes.STRING;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 15:46
 **/
public class SlothTableScanOperator extends AbstractOperator {
    public static final List<Value> EOF = null;

    private RelOptTable table;

    //MOCK
    private Iterator<List<Value>> iterator;
    private List<DataType> dataTypes;

    public SlothTableScanOperator(RelOptTable table, RelDataType rowType) {
        super(rowType);
        this.table = table;
    }

    @Override
    public void open() {

        //TODO currently data is mock
        List<List<Value>> valuesLists = Lists.newArrayList();
        Random random = new Random();
        if (table.getQualifiedName().contains("person")) {
            List<Value> v1 = Lists.newArrayList(new Value(1L, LONG), new Value("hello", STRING));
            List<Value> v2 = Lists.newArrayList(new Value(2L, LONG), new Value("good", STRING));
            List<Value> v3 = Lists.newArrayList(new Value(3L, LONG), new Value("nice", STRING));

            valuesLists.add(v1);
            valuesLists.add(v2);
            valuesLists.add(v3);

//            for (int i = 0; i < 100; i++) {
//                List<Value> v = Lists.newArrayList(new Value(random.nextInt(30), LONG), new Value(random.nextInt(30), STRING));
//                valuesLists.add(v);
//            }

        } else {

            List<Value> v1 = Lists.newArrayList(
                    new Value(100L, LONG),
                    new Value(3L, LONG),
                    new Value(100L, LONG),
                    new Value(25.5, DOUBLE),
                    new Value("zhangsan", STRING));

            List<Value> v2 = Lists.newArrayList(
                    new Value(101L, LONG),
                    new Value(2L, LONG),
                    new Value(55L, LONG),
                    new Value(32.5, DOUBLE),
                    new Value("lisi", STRING));

            List<Value> v3 = Lists.newArrayList(
                    new Value(102L, LONG),
                    new Value(5L, LONG),
                    new Value(23L, LONG),
                    new Value(45, DOUBLE),
                    new Value("wangwu", STRING));


            valuesLists.add(v1);
            valuesLists.add(v2);
            valuesLists.add(v3);

//            for (int i = 0; i < 100; i++) {
//                List<Value> v1 = Lists.newArrayList(
//                        new Value(100L, LONG),
//                        new Value(3L, LONG),
//                        new Value(100L, LONG),
//                        new Value(25.5, DOUBLE),
//                        new Value("zhangsan", STRING));
//
//                List<Value> v2 = Lists.newArrayList(
//                        new Value(101L, LONG),
//                        new Value(2L, LONG),
//                        new Value(55L, LONG),
//                        new Value(32.5, DOUBLE),
//                        new Value("lisi", STRING));
//
//                List<Value> v3 = Lists.newArrayList(
//                        new Value(102L, LONG),
//                        new Value(5L, LONG),
//                        new Value(23L, LONG),
//                        new Value(45, DOUBLE),
//                        new Value("wangwu", STRING));
//
//
//                valuesLists.add(v1);
//                valuesLists.add(v2);
//                valuesLists.add(v3);
//            }
        }

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
