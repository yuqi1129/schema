package com.yuqi.schema.common.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:43
 **/
public class FilterOperator extends AbstractOperator {

    public FilterOperator(Operator next) {
        super(next);
    }

    @Override
    public List<Value> getValue() {
        return super.getValue();
    }

    @Override
    public Operator next() {
        return next;
    }

    @Override
    public void init() {

        super.init();
    }

    @Override
    public void close() {
        super.close();
    }
}
