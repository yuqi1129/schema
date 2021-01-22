package com.yuqi.schema.raft;

import com.yuqi.schema.raft.generated.StorageServerRegisterProtos;
import com.yuqi.schema.raft.generated.StorageServerRegisterServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:29
 **/
public class StorageServerService extends StorageServerRegisterServiceGrpc.StorageServerRegisterServiceImplBase {
  @Override
  public void registerStorageServer(StorageServerRegisterProtos.StorageServerRegisterRequest request,
                                    StreamObserver<StorageServerRegisterProtos.StorageServerRegisterReponse> responseObserver) {
    StorageServerRegisterProtos.StorageServerRegisterReponse r =
        StorageServerRegisterProtos.StorageServerRegisterReponse.newBuilder().setCode(0).build();
    responseObserver.onNext(r);
  }
}
