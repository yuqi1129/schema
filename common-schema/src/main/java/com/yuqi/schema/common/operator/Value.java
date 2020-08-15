package com.yuqi.schema.common.operator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:21
 **/
public interface Value<T> {
    /**
     *
     * @return
     */
    Long getLong();


    /**
     *
     * @return
     */
    Integer getInt();


    T value();
}
