package com.yuqi.engine.plan;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:24
 **/
public enum JoinType {

    /**
     *
     */
    INNER_JOIN(0),

    /**
     *
     */
    LEFT_JOIN(1),

    /**
     *
     */
    RIGHT_JOIN(2),

    /**
     *
     */
    CROSS_JOIN(3),

    /**
     *
     */
    FULL_JOIN(4);

    private final int index;

    JoinType(int index) {
        this.index = index;
    }
}
