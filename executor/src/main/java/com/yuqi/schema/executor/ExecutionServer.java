package com.yuqi.schema.executor;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yuqi.schema.raft.Startable;
import com.yuqi.schema.raft.conn.HostAndPort;
import com.yuqi.schema.raft.grpc.GrpcServer;
import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:06
 **/
public class ExecutionServer extends GrpcServer implements Startable {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionServer.class);
  private static final Map<HostAndPort, StorageServerGrpcClient> RPC_CLIENT_MAP = Maps.newHashMap();
  private Set<HostAndPort> hostSets = Sets.newHashSetWithExpectedSize(10);

  public ExecutionServer(int port) {
    super(port);
  }

  public Set<HostAndPort> getHostSets() {
    return hostSets;
  }

  @Override
  public List<BindableService> getService() {
    return Collections.singletonList(new StorageServerRegister(this));
  }

  @Override
  public void start() {
    //start rpc
    startRpc();

    //block
    block();
  }
}
