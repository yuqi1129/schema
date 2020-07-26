package com.yuqi.protocol.connection;

import io.netty.channel.ChannelHandlerContext;

import java.util.Properties;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 13/7/20 23:25
 **/
public class ConnectionContext {
    private ChannelHandlerContext channelHandlerContext;

    /**
     * if db is not null, this context may use db;
     */
    private String db;

    private Properties properties = new Properties();

    public ConnectionContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }
}
