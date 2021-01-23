package com.yuqi.schema.register.grpc;

import com.yuqi.schema.raft.conn.HostAndPort;
import com.yuqi.schema.raft.generated.RegisterAndHeartBeatProtos;
import com.yuqi.schema.raft.generated.RegisterAndHeartBeatServiceGrpc;
import com.yuqi.schema.register.RegisterAndHeatBeatHandler;
import io.grpc.stub.StreamObserver;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:29
 **/
public class RegisterAndHeartBeatService extends RegisterAndHeartBeatServiceGrpc.RegisterAndHeartBeatServiceImplBase {
  private RegisterAndHeatBeatHandler registerAndHeatBeatHandler;

  public RegisterAndHeartBeatService(RegisterAndHeatBeatHandler registerAndHeatBeatHandler) {
    this.registerAndHeatBeatHandler = registerAndHeatBeatHandler;
  }

  @Override
  public void registerNodeInfo(RegisterAndHeartBeatProtos.NodeRegisterRequest request,
                               StreamObserver<RegisterAndHeartBeatProtos.NodeRegisterReponse> responseObserver) {
    RegisterAndHeartBeatProtos.NodeRegisterReponse r =
        RegisterAndHeartBeatProtos.NodeRegisterReponse.newBuilder().setCode(0).build();
    final String host = request.getHostname();
    final int port = request.getPort();

    registerAndHeatBeatHandler.addNodeInfo(new HostAndPort(host, port));
    responseObserver.onNext(r);
    responseObserver.onCompleted();
  }
}
