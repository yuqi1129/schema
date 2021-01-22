package com.yuqi.schema.raft;

import com.yuqi.schema.raft.generated.StorageServerRegisterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午7:03
 **/
public class GrpcClient implements AutoCloseable, Startable {
  public static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);

  protected String host;
  protected int port;

  protected ManagedChannel managedChannel;
  protected StorageServerRegisterServiceGrpc.StorageServerRegisterServiceBlockingStub stub;
  public GrpcClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public void start() {
    managedChannel = ManagedChannelBuilder.forAddress(host, port)
        .build();

    stub = StorageServerRegisterServiceGrpc.newBlockingStub(managedChannel);
  }

  @Override
  public void close() throws Exception {
    if (managedChannel != null) {
      managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
