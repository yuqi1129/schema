package com.yuqi.schema.common.enumerator;

import org.apache.calcite.linq4j.Enumerator;

import java.util.Iterator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 16:54
 **/
public class BasicEnumerator implements Enumerator<Object[]> {

    private Iterator<Object[]> iterator;

    public BasicEnumerator(Iterator<Object[]> iterator) {
        this.iterator = iterator;
    }

    private Object[] current;
    @Override
    public Object[] current() {
        return current;
    }

    @Override
    public boolean moveNext() {
        if (iterator.hasNext()) {
            current = iterator.next();
            return true;
        } else {
            current = null;
            return false;
        }
    }

    @Override
    public void reset() {
        //do nothing
    }

    @Override
    public void close() {
        //do nothing
    }
}
