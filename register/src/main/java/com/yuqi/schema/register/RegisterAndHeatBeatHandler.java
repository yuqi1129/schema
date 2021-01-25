package com.yuqi.schema.register;

import com.google.common.collect.Sets;
import com.yuqi.schema.raft.conn.HostAndPort;

import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 23/1/21 下午11:35
 **/
public class RegisterAndHeatBeatHandler {
  private Set<HostAndPort> hostSets = Sets.newHashSetWithExpectedSize(10);

  public Set<HostAndPort> getHostSets() {
    return hostSets;
  }

  public void addNodeInfo(HostAndPort ip) {
    hostSets.add(ip);
  }
}
