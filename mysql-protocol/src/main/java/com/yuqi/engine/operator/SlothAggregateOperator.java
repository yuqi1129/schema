package com.yuqi.engine.operator;

import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.io.IO;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:38
 **/
public class SlothAggregateOperator implements Operator, IO {

    private Operator child;

    @Override
    public void open() {
        child.open();
    }

    @Override
    public List<Value> next() {
        //block, util we handle all row from child
        return null;
    }

    @Override
    public void close() {
        child.close();
    }
}
