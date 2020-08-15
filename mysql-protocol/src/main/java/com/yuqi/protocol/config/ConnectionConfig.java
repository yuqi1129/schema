package com.yuqi.protocol.config;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/7/20 11:37
 **/
public class ConnectionConfig {

    /**
     * Unit seconds
     *
     * Connection read time out
     */
    public static final int readTimeOut = 20 * 60;

    /**
     * Unit seconds
     *
     * Connection write time out;
     */
    public static final int writeTimeout = 10 * 60;
}
