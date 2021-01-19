package com.yuqi.schema.executor;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.yuqi.schema.raft.generated.StorageServerRegisterProtos;

import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:38
 **/
public class StorageServerRegister extends StorageServerRegisterProtos.StorageServerRegisterService {
  private Set<String> storageService;

  public StorageServerRegister(Set<String> storageService) {
    this.storageService = storageService;
  }

  public Set<String> getStorageService() {
    return storageService;
  }

  @Override
  public void registerStorageServer(RpcController controller, StorageServerRegisterProtos.StorageServerRegisterRequest request,
                                    RpcCallback<StorageServerRegisterProtos.StorageServerRegisterReponse> done) {

    String hostAndPort = request.getHostnameAndPort();
    storageService.add(hostAndPort);
    //check valid
    if (isValid(hostAndPort)) {
      //todo
    }
  }

  private boolean isValid(String hostName) {
    return true;
  }
}
