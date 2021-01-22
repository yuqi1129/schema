package com.yuqi.schema.raft;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午7:51
 **/
public class StorageServer implements AutoCloseable, Startable {
  public static final Logger LOGGER = LoggerFactory.getLogger(StorageServer.class);
  private final static ExecutorService SERVICE_PRC_POOL = Executors.newSingleThreadExecutor();
  private int storagePort;
  private String exectionServerAddr;
  private int exectionServerPort;
  private Server server;
  private ExecutionServerRpcClient rpcClient;
  public StorageServer(int storagePort, String exectionServerAddr, int exectionServerPort) {
    this.storagePort = storagePort;
    this.exectionServerAddr = exectionServerAddr;
    this.exectionServerPort = exectionServerPort;
  }

  @Override
  public void start() {
    //SERVICE_PRC_POOL.submit(this::startRpc);
    startRpc();
    //start and register to execution;
    initExecutioClient();
    //maybe should be register peroidiacally
    registerToExecutionServer();

    //wait
    block();
  }

  private void startRpc() {
    try {
      server = ServerBuilder.forPort(storagePort)
          .addService(new StorageServerService())
          .build().start();
    } catch (Exception e) {
      LOGGER.error("Start StorageServer meet error: ", e);
    }
  }

  private void initExecutioClient() {
    if (rpcClient == null) {
      rpcClient = new ExecutionServerRpcClient(exectionServerAddr, exectionServerPort);
    }
    rpcClient.start();
  }

  private void registerToExecutionServer() {
    String localHost = "127.0.0.1";
    rpcClient.registerLocation(localHost, storagePort);
  }

  @Override
  public void close() throws Exception {
    if (!server.isShutdown()) {
      server.shutdownNow();
    }
  }

  public void block() {
    try {
      Thread.sleep(Long.MAX_VALUE);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
