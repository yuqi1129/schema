package com.yuqi.schema.raft;

import com.yuqi.schema.raft.generated.StorageServerRegisterProtos;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午7:56
 **/
public class ExecutionServerRpcClient extends GrpcClient {
  public ExecutionServerRpcClient(String host, int port) {
    super(host, port);
  }

  public void registerLocation(String host, int port) {
    StorageServerRegisterProtos.StorageServerRegisterRequest.Builder builder
        = StorageServerRegisterProtos.StorageServerRegisterRequest.newBuilder();

    builder.setHostname(host);
    builder.setPort(port);

    StorageServerRegisterProtos.StorageServerRegisterReponse r =
        stub.registerStorageServer(builder.build());

    if (r.getCode() == 0) {
      LOGGER.info("Register stoarge successful to execution server...");
    } else {
      LOGGER.error("Failed register...");
    }
  }
}
