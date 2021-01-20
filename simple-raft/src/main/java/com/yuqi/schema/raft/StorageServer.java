package com.yuqi.schema.raft;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午7:51
 **/
public class StorageServer {
  public static void main(String[] args) {
    StorageServer storageServer = new StorageServer();


    storageServer.registerRpcServer();
  }

  private void registerRpcServer() {
    //reister rpc address to mananger
  }
}
