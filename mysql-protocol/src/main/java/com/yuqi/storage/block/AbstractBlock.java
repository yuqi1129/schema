package com.yuqi.storage.block;

import com.yuqi.engine.io.IO;
import java.util.BitSet;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 16:43
 **/
public abstract class AbstractBlock<T> implements IO {

//    public static final Unsafe UNSAFE = UnsafeUtils.getUnsafe();
    //todo
    protected long base;
    protected int size;
    protected int currentSize;

    /**
     * true means is null
     */
    protected BitSet nullBitSet;

    public AbstractBlock(int size) {
        this.size = size;
//        base = UNSAFE.allocateMemory(size);
        currentSize = 0;

        nullBitSet = new BitSet(size);
    }

    public abstract boolean canAdd();

    public abstract void add(T v);

    public void free() {
//        UNSAFE.freeMemory(base);
    }

    /**
     * 读取第postion位置上的 value
     * <p>
     * 比如说一个block 有1024个数，pos = 0 代表读取第0个数
     *
     * @param index
     * @return
     */
    public abstract T read(int index);
}
