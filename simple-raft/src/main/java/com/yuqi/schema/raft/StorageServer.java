package com.yuqi.schema.raft;

import com.yuqi.schema.raft.grpc.GrpcServer;
import com.yuqi.schema.raft.grpc.RegisterAndHeartBeatRpcClient;
import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午7:51
 **/
public class StorageServer extends GrpcServer implements AutoCloseable, Startable {
  public static final Logger LOGGER = LoggerFactory.getLogger(StorageServer.class);
  private String exectionServerAddr;
  private int exectionServerPort;
  private RegisterAndHeartBeatRpcClient rpcClient;
  public StorageServer(int storagePort, String exectionServerAddr, int exectionServerPort) {
    super(storagePort);
    this.exectionServerAddr = exectionServerAddr;
    this.exectionServerPort = exectionServerPort;
  }

  @Override
  public void start() {
    startRpc();
    //start and register to execution;
    initExecutionClient();
    //maybe should be register peroidiacally
    registerToExecutionServer();

    //wait
    block();
  }

  private void initExecutionClient() {
    if (rpcClient == null) {
      rpcClient = new RegisterAndHeartBeatRpcClient(exectionServerAddr, exectionServerPort);
    }
    rpcClient.start();
  }

  @Override
  public List<BindableService> getService() {
    return Collections.emptyList();
    //return Collections.singletonList(new RegisterAndHeartBeatService());
  }

  private void registerToExecutionServer() {
    String localHost = "127.0.0.1";
    rpcClient.registerLocation(localHost, port);
  }

  @Override
  public void close() throws Exception {
    if (!server.isShutdown()) {
      server.shutdownNow();
    }
  }
}
