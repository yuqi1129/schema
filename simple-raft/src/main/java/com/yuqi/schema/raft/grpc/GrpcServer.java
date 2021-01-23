package com.yuqi.schema.raft.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 23/1/21 下午10:35
 **/
public abstract class GrpcServer implements AutoCloseable {
  public static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);

  protected int port;
  protected Server server;

  public GrpcServer(int port) {
    this.port = port;
  }

  public void startRpc() {
    ServerBuilder<?> builder = ServerBuilder.forPort(port);
    List<BindableService> serviceLists = getService();
    serviceLists.forEach(builder::addService);
    server = builder.build();
    try {
      server.start();
    } catch (Exception e) {
      LOGGER.error("Start StorageServer meet error: ", e);
    }
  }

  public abstract List<BindableService> getService();


  public void block() {
    try {
      Thread.sleep(Long.MAX_VALUE);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public void close() throws Exception {
    if (!server.isShutdown()) {
      server.shutdownNow();
    }
  }
}
