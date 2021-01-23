package com.yuqi.schema.register.grpc;

import com.yuqi.schema.raft.generated.DdlProtos;
import com.yuqi.schema.raft.generated.DdlServiceGrpc;
import com.yuqi.schema.raft.grpc.GrpcClient;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午7:59
 **/
public class DdlServiceClient extends GrpcClient {
  private DdlServiceGrpc.DdlServiceBlockingStub stub;

  public DdlServiceClient(String host, int port) {
    super(host, port);
  }

  private void executeCreateTable(String tableName) {
    if (stub == null) {
      stub = DdlServiceGrpc.newBlockingStub(managedChannel);
    }

    DdlProtos.CreateTableRequest.Builder b = DdlProtos.CreateTableRequest.newBuilder();
    b.setTableName(tableName);
    DdlProtos.CreateTableResponse r = stub.createTable(b.build());
  }

}
