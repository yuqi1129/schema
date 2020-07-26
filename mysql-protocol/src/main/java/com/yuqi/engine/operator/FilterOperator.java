package com.yuqi.engine.operator;

import com.yuqi.engine.io.IO;

import java.util.List;

import static com.yuqi.engine.operator.TableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:15
 **/
public class FilterOperator implements Operator, IO {

    private List<String> filterCondition;
    private Operator child;

    @Override
    public void open() {
        child.open();
    }

    @Override
    public List<Object> next() {
        while (true) {
            List<Object> result = child.next();
            if (result != EOF && isTrue(result)) {
                return result;
            }

            if (result == EOF) {
                return EOF;
            }
        }
    }

    @Override
    public void close() {
        child.close();
    }

    private boolean isTrue(List<Object> row) {
        return true;
    }
}
