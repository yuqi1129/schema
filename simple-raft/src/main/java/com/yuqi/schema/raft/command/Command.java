package com.yuqi.schema.raft.command;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午7:51
 **/
public interface Command {
  CommandDetail getCommand();
}
