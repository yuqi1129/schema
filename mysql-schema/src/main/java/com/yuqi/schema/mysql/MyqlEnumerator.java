package com.yuqi.schema.mysql;

import lombok.RequiredArgsConstructor;
import org.apache.calcite.linq4j.Enumerator;

import java.util.Iterator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 21:08
 **/
@RequiredArgsConstructor
public class MyqlEnumerator implements Enumerator<Object[]> {

    private final Iterator<Object[]> iterator;
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
        }

        current = null;
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void close() {

    }
}
