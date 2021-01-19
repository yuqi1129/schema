package com.yuqi.scheme.storage;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 15/1/21 下午8:05
 **/
public interface Block<T> {

  T read(int pos);

  void write(T value);

  void flush();

  /**
   * Get all Match index
   * @param query query Condition
   * @return
   */
  //List<Integer> getIndex(Query query);

  /**
   *
   * @param index
   * @return
   */
  List<T> getValue(List<Integer> index);
}
