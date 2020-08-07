package com.yuqi.engine.data.func.agg;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 7/8/20 16:31
 **/
public interface Aggregation {

    /**
     * compute result
     * @return
     */
    Value compute();


    /**
     *
     * @return
     */
    DataType getResultType();


    /**
     *
     * @return
     */
    DataType inputType();


    /**
     *
     * @return
     */
    boolean isDistinct();


    /**
     *
     * @return
     */
    boolean ignoreNulls();


    /**
     * add a Value
     * @param v
     */
    default void add(Value v) {
        //todo
    }

    /**
     *
     */
    void init();

}
