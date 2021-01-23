package com.yuqi.schema.register;

import com.google.common.collect.Maps;
import com.yuqi.schema.raft.Startable;
import com.yuqi.schema.raft.conn.HostAndPort;
import com.yuqi.schema.raft.grpc.GrpcServer;
import com.yuqi.schema.register.grpc.DdlServiceClient;
import com.yuqi.schema.register.grpc.RegisterAndHeartBeatService;
import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:06
 **/
public class ConfigurationCenter extends GrpcServer implements Startable {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationCenter.class);
  private static final Map<HostAndPort, DdlServiceClient> RPC_CLIENT_MAP = Maps.newHashMap();
  private RegisterAndHeatBeatHandler registerAndHeatBeatHandler = new RegisterAndHeatBeatHandler();

  public ConfigurationCenter(int port) {
    super(port);
  }

  @Override
  public List<BindableService> getService() {
    return Collections.singletonList(new RegisterAndHeartBeatService(registerAndHeatBeatHandler));
  }

  @Override
  public void start() {
    //start rpc
    startRpc();

    //block
    block();
  }
}
