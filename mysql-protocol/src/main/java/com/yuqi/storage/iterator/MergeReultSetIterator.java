package com.yuqi.storage.iterator;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 17:03
 **/
public class MergeReultSetIterator<T> implements ResultSetIterator<T> {
    //merge realtime engine and block reader


    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        return null;
    }
}
