package com.yuqi.schema.executor;

import com.yuqi.schema.raft.generated.StorageServerRegisterProtos;
import com.yuqi.schema.raft.generated.StorageServerRegisterServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:38
 **/
public class StorageServerRegister extends StorageServerRegisterServiceGrpc
    .StorageServerRegisterServiceImplBase {
  private Set<String> storageService;

  public StorageServerRegister(Set<String> storageService) {
    this.storageService = storageService;
  }

  public Set<String> getStorageService() {
    return storageService;
  }

  @Override
  public void registerStorageServer(StorageServerRegisterProtos.StorageServerRegisterRequest request,
                                    StreamObserver<StorageServerRegisterProtos.StorageServerRegisterReponse> responseObserver) {
    //super.registerStorageServer(request, responseObserver);
    String hostAndPort = request.getHostnameAndPort();
    storageService.add(hostAndPort);
    //check valid
    if (isValid(hostAndPort)) {
      //todo
    }

    StorageServerRegisterProtos.StorageServerRegisterReponse.Builder builder =
        StorageServerRegisterProtos.StorageServerRegisterReponse.newBuilder();
    builder.setCode(1);
    responseObserver.onNext(builder.build());

  }

  private boolean isValid(String hostName) {
    return true;
  }
}
