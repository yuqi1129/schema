package com.yuqi.schema.raft;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.yuqi.schema.raft.generated.StorageServerProtos;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:29
 **/
public class StorageServerService extends StorageServerProtos.StorageServerService {

  @Override
  public void createTable(RpcController controller, StorageServerProtos.CreateTableRequest request,
                          RpcCallback<StorageServerProtos.CreateTableResponse> done) {
    StorageServerProtos.CreateTableResponse.Builder responseBuilder =
        StorageServerProtos.CreateTableResponse.newBuilder();

    //success
    responseBuilder.setCode(0);
    done.run(responseBuilder.build());
  }
}
