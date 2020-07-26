package com.yuqi.schema.common.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/5/20 21:26
 **/
public class TableScanOperator extends AbstractOperator {

    public TableScanOperator(Operator next) {
        super(next);
    }

    private Table table;

    /*
        Table name
        filter condition
        and so on
     */

    @Override
    public Operator next() {
        return null;
    }

    @Override
    public void init() {
        table = new FileTable();
        super.init();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public List<Value> getValue() {
        return table.query();
    }
}
