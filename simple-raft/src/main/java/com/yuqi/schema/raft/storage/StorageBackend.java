package com.yuqi.schema.raft.storage;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 26/1/21 下午8:59
 **/
public interface StorageBackend {

  default void store() {
    return;
  }

  default void query() {
    return;
  }
}
