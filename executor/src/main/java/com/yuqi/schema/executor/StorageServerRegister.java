package com.yuqi.schema.executor;

import com.yuqi.schema.raft.conn.HostAndPort;
import com.yuqi.schema.raft.generated.StorageServerRegisterProtos;
import com.yuqi.schema.raft.generated.StorageServerRegisterServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:38
 **/
public class StorageServerRegister extends StorageServerRegisterServiceGrpc
    .StorageServerRegisterServiceImplBase {
  private ExecutionServer executionServer;

  public StorageServerRegister(ExecutionServer storageService) {
    this.executionServer = storageService;
  }

  @Override
  public void registerStorageServer(StorageServerRegisterProtos.StorageServerRegisterRequest request,
                                    StreamObserver<StorageServerRegisterProtos.StorageServerRegisterReponse> responseObserver) {

    final String host = request.getHostname();
    final int port = request.getPort();
    executionServer.getHostSets().add(new HostAndPort(host, port));

    StorageServerRegisterProtos.StorageServerRegisterReponse.Builder builder =
        StorageServerRegisterProtos.StorageServerRegisterReponse.newBuilder();
    builder.setCode(0);
    responseObserver.onNext(builder.build());
  }
}
