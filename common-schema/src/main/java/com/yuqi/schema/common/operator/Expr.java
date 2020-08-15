package com.yuqi.schema.common.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:45
 **/
public interface Expr {

    /**
     *
     * @return
     */
    int getInputParameterCount();

    /**
     *
     * @return
     */
    List<Value> getParameter();

    /**
     *
     * @return
     */
    Value getResult();


    /**
     * compute result
     * @return
     */
    Value compute();

}
