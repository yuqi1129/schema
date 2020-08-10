package com.yuqi.storage.block;

import java.util.Objects;

import static com.yuqi.storage.constant.TypeConstants.INT_SIZE;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:45
 **/
public class IntBlock extends AbstractBlock<Integer> {

    public IntBlock(int size) {
        super(size * INT_SIZE);
    }

    @Override
    public boolean canAdd() {
        return currentSize + 1 <= BlockConstants.BLOCK_SIZE;
    }

    @Override
    public void add(Integer v) {
        if (Objects.isNull(v)) {
            nullBitSet.set(currentSize++);
            return;
        }

        UNSAFE.putInt(base + currentSize * INT_SIZE, v);
        currentSize++;
    }

    @Override
    public Integer read(int pos) {
        if (nullBitSet.get(pos)) {
            return null;
        }

        return UNSAFE.getInt(base + pos * INT_SIZE);
    }
}
