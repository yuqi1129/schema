package com.yuqi.schema.common.operator;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/5/20 21:34
 **/
public class ProjectOperator extends AbstractOperator {

    private List<Integer> index;

    public ProjectOperator(Operator next) {
        super(next);
    }

    @Override
    public List<Value> getValue() {
        final List<Value> childValue = next().getValue();

        List<Value> result = Lists.newArrayList();

        int k = 0;
        int columnSize = childValue.size();
        for (int i = 0; i < columnSize; i++) {
            if (index.get(k) == i) {
                result.add(childValue.get(i));
                k++;
            }
        }

        return result;
    }

    @Override
    public Operator next() {
        return next;
    }

    @Override
    public void init() {
        //只取tableScan原来
        //这里需要根据project拿到对应的映射关系
        index = Lists.newArrayList(0, 1, 2);
        super.init();
    }

    @Override
    public void close() {
        super.close();
    }
}
