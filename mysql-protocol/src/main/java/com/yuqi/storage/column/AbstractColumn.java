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

    private List<AbstractBlock> blocks;

    abstract void put(T value);
}
