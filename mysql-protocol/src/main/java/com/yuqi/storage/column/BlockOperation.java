package com.yuqi.storage.column;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 10/8/20 17:44
 **/
public interface BlockOperation {

    /**
     * Insert a value
     *
     * @return
     */
    boolean insert();

    /**
     * delete value
     *
     * @return
     */
    boolean delete();


    //TODO some other method

}
