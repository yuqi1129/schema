package com.yuqi.storage.column;

import com.yuqi.storage.block.AbstractBlock;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:50
 **/
public class StringColumn extends AbstractColumn<String> {

    public StringColumn(List<AbstractBlock> blocks, String defaultValue, boolean notNull) {
        super(blocks, defaultValue, notNull);
    }

    @Override
    public void put(String value) {
        //todo
    }
}
