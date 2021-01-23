package com.yuqi.schema.raft.grpc;

import com.yuqi.schema.raft.Startable;
import com.yuqi.schema.raft.generated.StorageServerRegisterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
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
  protected boolean useSSL = false;

  protected ManagedChannel managedChannel;
  protected StorageServerRegisterServiceGrpc.StorageServerRegisterServiceBlockingStub stub;
  public GrpcClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void setUseSSL(boolean useSSL) {
    this.useSSL = useSSL;
  }

  @Override
  public void start() {
    managedChannel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build();


    if (useSSL) {
      try {
        managedChannel = NettyChannelBuilder
            .forAddress(host, port)
            .sslContext(GrpcSslContexts.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build()).build();
      } catch (SSLException e) {
        LOGGER.error("Start client:", e);
        throw new RuntimeException(e);
      }
    } else {
      managedChannel = ManagedChannelBuilder.forAddress(host, port)
          .usePlaintext()
          .build();
    }

    stub = StorageServerRegisterServiceGrpc.newBlockingStub(managedChannel);
  }

  @Override
  public void close() throws Exception {
    if (managedChannel != null) {
      managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
