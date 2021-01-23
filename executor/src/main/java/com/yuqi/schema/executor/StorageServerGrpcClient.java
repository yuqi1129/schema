package com.yuqi.schema.executor;

import com.yuqi.schema.raft.grpc.GrpcClient;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午7:59
 **/
public class StorageServerGrpcClient extends GrpcClient {
  public StorageServerGrpcClient(String host, int port) {
    super(host, port);
  }
}
