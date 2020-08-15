package com.yuqi.sql;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/7/20 19:50
 **/
public interface LifeCycle {


    void init();


    default void start() {
        //do nothing
    }

    void close();
}
