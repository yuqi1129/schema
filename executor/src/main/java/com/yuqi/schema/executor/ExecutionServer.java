package com.yuqi.schema.executor;

import com.google.common.collect.Sets;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/21 下午8:06
 **/
public class ExecutionServer {

  public static void main(String[] args) throws InterruptedException, IOException {
    Set<String> hostSets = Sets.newHashSetWithExpectedSize(10);
    StorageServerRegister storageServerRegister = new StorageServerRegister(hostSets);

    Server server = ServerBuilder.forPort(3301)
        .addService(storageServerRegister).build().start();

    server.awaitTermination();
  }
}
