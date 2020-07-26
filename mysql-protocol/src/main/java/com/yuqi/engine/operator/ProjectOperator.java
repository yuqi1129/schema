package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.io.IO;

import java.util.List;

import static com.yuqi.engine.operator.TableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:04
 **/
public class ProjectOperator implements Operator, IO {

    private List<String> rowType;

    /**
     * Mapping operation
     */
    private List<Integer> mapping;

    private Operator child;

    //

    @Override
    public void open() {
        child.open();
    }

    @Override
    public List<Object> next() {
        List<Object> result = child.next();

        if (result == EOF) {
            return result;
        }

        int size = mapping.size();

        List<Object> list = Lists.newArrayList(size);
        for (int i = 0; i < size; i++) {
            list.add(result.get(mapping.get(i)));
        }

        return list;
    }

    @Override
    public void close() {
        child.close();
    }
}
