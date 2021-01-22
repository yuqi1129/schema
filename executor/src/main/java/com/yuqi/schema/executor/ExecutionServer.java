package com.yuqi.schema.executor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yuqi.schema.raft.Startable;
import com.yuqi.schema.raft.conn.HostAndPort;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:06
 **/
public class ExecutionServer implements AutoCloseable, Startable {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionServer.class);
  private static final Map<HostAndPort, StorageServerGrpcClient> RPC_CLIENT_MAP = Maps.newHashMap();
  private Set<HostAndPort> hostSets = Sets.newHashSetWithExpectedSize(10);
  private int port;
  private Server server;

  public ExecutionServer(int port) {
    this.port = port;
  }

  public Set<HostAndPort> getHostSets() {
    return hostSets;
  }

  private void startRpc() {
    try {
      StorageServerRegister storageServerRegister = new StorageServerRegister(this);
      server = ServerBuilder.forPort(port)
          .addService(storageServerRegister).build().start();

      //server.awaitTermination();
    } catch (Exception e) {
      LOGGER.error("Start ExecutionServer meet:", e);
    }
  }

  @Override
  public void start() {
    startRpc();

    block();
  }

  @Override
  public void close() throws Exception {
    if (server != null) {
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
