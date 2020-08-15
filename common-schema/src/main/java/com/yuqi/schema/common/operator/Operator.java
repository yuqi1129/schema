package com.yuqi.schema.common.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:21
 **/
public interface Operator {


    List<Value> getValue();

    /**
     * do init() work
     */
    void init();

    /**
     * main work
     * @return
     */
    Operator next();

    /**
     * do close work
     */
    void close();
}


