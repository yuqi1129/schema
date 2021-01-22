package com.yuqi.schema.raft;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午7:21
 **/
public class StorageServerMain {
  public static void main(String[] args) {
    int storageServerPort = 4100;
    String executionServerHost = "localhost";
    int executionServerPort = 3100;

    StorageServer storageServer = new StorageServer(storageServerPort, executionServerHost, executionServerPort);
    storageServer.start();
  }
}
