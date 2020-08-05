package com.yuqi.engine.data.expr;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 15:53
 **/
public interface Input<T> {
    //compute the result;
    T value();
}
