package com.yuqi.engine.operator;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 15:38
 **/
public interface Operator {

    void open();

    /**
     * Get next row, next has another value to identify the value type
     * @return
     */
    List<Value> next();

    void close();

    default List<DataType> getRowType() {
        //TODO
        return null;
    }
}
