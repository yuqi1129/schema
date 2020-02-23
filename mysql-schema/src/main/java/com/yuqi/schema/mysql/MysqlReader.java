package com.yuqi.schema.mysql;

import java.util.Iterator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:28
 **/
public interface MysqlReader {

    /**
     * MySQL reader to read data
     * @return
     */
    Iterator<Object[]> readData();
}
