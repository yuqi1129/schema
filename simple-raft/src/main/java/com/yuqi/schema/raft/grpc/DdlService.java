package com.yuqi.schema.raft.grpc;

import com.yuqi.schema.raft.generated.DdlProtos;
import com.yuqi.schema.raft.generated.DdlServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 23/1/21 下午11:50
 **/
public class DdlService extends DdlServiceGrpc.DdlServiceImplBase {


  @Override
  public void createTable(DdlProtos.CreateTableRequest request, StreamObserver<DdlProtos.CreateTableResponse> responseObserver) {
    super.createTable(request, responseObserver);
  }
}
