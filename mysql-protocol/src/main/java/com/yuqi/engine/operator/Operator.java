package com.yuqi.engine.operator;

import com.yuqi.engine.data.type.DataType;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 15:38
 **/
public interface Operator<R> {

    /**
     * Open operator, do some init work
     */
    void open();

    /**
     * Get next row, next has another value to identify the value type
     *
     * R is the row type
     * @return
     */
    R next();

    /**
     * do close work, eg, you can close resource/network
     */
    void close();

    /**
     * Get return type of this operator
     * @return
     */
    default List<DataType> getRowType() {
        //TODO
        return null;
    }
}
