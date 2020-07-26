package com.yuqi.engine.operator;

import com.yuqi.engine.io.IO;
import com.yuqi.engine.plan.JoinType;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:22
 **/
public class JoinOperator implements Operator, IO {

    private JoinType joinType;
    private List<String> joinCondition;
    private List<String> rowType;

    private Operator left;
    private Operator right;


    @Override
    public void open() {
        left.open();
        right.open();
    }

    @Override
    public List<Object> next() {
        return null;
    }

    @Override
    public void close() {
        left.close();
        right.close();
    }
}
