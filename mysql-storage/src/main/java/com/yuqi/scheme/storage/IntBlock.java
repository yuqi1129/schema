package com.yuqi.scheme.storage;

import com.yuqi.engine.data.value.IntValue;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 15/1/21 下午8:09
 **/
public class IntBlock extends AbstractBlock<IntValue> {
  public IntBlock(int blockSize) {
    super(blockSize);
    startOffset = 0L;
  }


  @Override
  public IntValue read(int pos) {
    return super.read(pos);
  }

  @Override
  public void write(IntValue value) {
    super.write(value);
  }

  @Override
  public void flush() {
    super.flush();
  }

  @Override
  public List<IntValue> getValue(List<Integer> index) {
    return null;
  }
}
