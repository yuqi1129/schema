package com.yuqi.schema.common.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/5/20 21:23
 **/
public abstract class AbstractOperator implements Operator {

    protected Operator next;

    public AbstractOperator(Operator next) {
        this.next = next;
    }

    @Override
    public List<Value> getValue() {
        return next().getValue();
    }

    @Override
    public Operator next() {
        throw new UnsupportedOperationException("current do not support");
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {

    }
}
