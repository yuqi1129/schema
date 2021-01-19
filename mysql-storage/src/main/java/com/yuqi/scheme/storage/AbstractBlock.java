package com.yuqi.scheme.storage;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 15/1/21 下午8:04
 **/
public abstract class AbstractBlock<T> implements Block<T> {
  protected int blockSize;
  protected long startOffset;
  protected long[] nullBitMap;

  //if delete, mark the corrpnsd bit to 1
  protected long[] deletedBitMap;

  protected T max;
  protected T min;
  protected int nullCount;
  protected int valueCount;

  //if block size is 128, the start line number may be 0, 128, 256...
  private int startLineNumber;

  public AbstractBlock(int blockSize) {
    this.blockSize = blockSize;
    nullBitMap = new long[blockSize / 64];
    nullCount = 0;
    valueCount = 0;
  }

  @Override
  public T read(int pos) {
    return null;
  }

  @Override
  public void write(T value) {

  }

  @Override
  public void flush() {

  }


  /**
   *
   * Table Shard ------> StorageEngine
   *
   * StorageEngine
   *              insert
   *              query
   *        MemoryStorge
   *            MemeryBlock
   *                insert
   *                query
   *                update
   *                ...
   *        DiskStorage
   *            List<inDiskBlock>
   *                  [0-127]
   *                  [128,255]
   *                  [256,...]
   *                  每个block保持相应的插入行顺序
   *
   *
   *
   */
}
