package com.yuqi.storage.column;

import com.yuqi.storage.block.AbstractBlock;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:46
 **/
public abstract class AbstractColumn<T> {

    protected List<AbstractBlock> blocks;
    /**
     * 默认值在插入的时候就补充好
     */
    protected T defaultValue;
    protected boolean notNull;
    /**
     * Start from 0;
     */
    protected int currentBlock;

    public AbstractColumn(List<AbstractBlock> blocks, T defaultValue, boolean notNull) {
        this.blocks = blocks;
        this.defaultValue = defaultValue;
        this.notNull = notNull;
    }

    public abstract void put(T value);
}
