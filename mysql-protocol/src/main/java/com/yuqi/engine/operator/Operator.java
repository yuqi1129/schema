package com.yuqi.engine.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 15:38
 **/
public interface Operator {

    void open();

    /**
     * Get next row, next has another value to identify the value type
     * @return
     */
    List<Object> next();

    void close();

    default List<String> getRowType() {
        //TODO
        return null;
    }
}
