package com.yuqi.engine.data;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 29/8/20 14:12
 **/
public interface Row<T> {

    /**
     * Size of Column
     * @return
     */
    int columnSize();

    /**
     *
     * @param i, index, start from 0
     * @return value of the column in index
     */
    T getColumn(int i);

    /**
     * Get all column of a row
     * @return
     */
    List<T> getAllColumn();
}
