package com.yuqi.storage.column;

import com.yuqi.storage.block.AbstractBlock;
import com.yuqi.storage.block.BlockConstants;
import com.yuqi.storage.block.IntBlock;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:50
 **/
public class IntegerColumn extends AbstractColumn<Integer> {

    public IntegerColumn(List<AbstractBlock> blocks, Integer defaultValue, boolean notNull) {
        super(blocks, defaultValue, notNull);
        currentBlock = blocks.size() - 1;
    }

    @Override
    public void put(Integer value) {
        IntBlock block;

        if (currentBlock == -1) {
            block = new IntBlock(BlockConstants.BLOCK_SIZE);
            currentBlock++;
            blocks.add(block);
        } else {
            block = (IntBlock) blocks.get(currentBlock);
        }

        if (block.canAdd()) {
            block = new IntBlock(BlockConstants.BLOCK_SIZE);
            blocks.add(block);
            currentBlock++;
        }

        block.add(value);
    }

}
