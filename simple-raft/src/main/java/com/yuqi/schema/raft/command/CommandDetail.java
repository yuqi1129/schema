package com.yuqi.schema.raft.command;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午7:54
 **/
public class CommandDetail {
  private final String key;
  private final String value;

  private final String table;

  public CommandDetail(String key, String value, String table) {
    this.key = key;
    this.value = value;
    this.table = table;
  }
}
