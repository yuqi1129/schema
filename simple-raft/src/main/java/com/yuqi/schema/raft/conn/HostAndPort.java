package com.yuqi.schema.raft.conn;

import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 22/1/21 下午8:15
 **/
public class HostAndPort {
  private final String ip;
  private final int port;

  public HostAndPort(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }

  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HostAndPort ipAndPort = (HostAndPort) o;
    return Objects.equals(ip, ipAndPort.ip)
        && Objects.equals(port, ipAndPort.port);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ip, port);
  }
}
