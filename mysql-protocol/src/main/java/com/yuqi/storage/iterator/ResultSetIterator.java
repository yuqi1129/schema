package com.yuqi.storage.iterator;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:58
 **/
public interface ResultSetIterator<T> {

    boolean hasNext();


    T next();
}
