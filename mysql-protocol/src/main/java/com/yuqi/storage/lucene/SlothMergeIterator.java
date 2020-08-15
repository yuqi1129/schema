package com.yuqi.storage.lucene;

import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/8/20 20:34
 **/
public class SlothMergeIterator<E> implements Iterator<E> {

    private List<Iterator<E>> allValues;
    private int index;
    private E current;

    private int valueSize;

    public SlothMergeIterator(List<Iterator<E>> allValues) {
        this.allValues = allValues;
        index = 0;
        current = null;

        valueSize = allValues.size();
    }

    @Override
    public boolean hasNext() {

        while (index < valueSize) {
            Iterator<E> it = allValues.get(index);
            if (it.hasNext()) {
                current = it.next();
                return true;
            }

            index++;
        }

        return false;
    }

    @Override
    public E next() {
        return current;
    }
}
