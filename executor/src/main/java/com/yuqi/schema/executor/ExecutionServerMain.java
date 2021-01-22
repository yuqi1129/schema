package com.yuqi.schema.executor;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午8:01
 **/
public class ExecutionServerMain {
  public static void main(String[] args) {
    //some parameter
    int port = 3100;
    ExecutionServer executionServer = new ExecutionServer(port);
    executionServer.start();
  }
}
