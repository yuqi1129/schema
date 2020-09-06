package com.yuqi.protocol.connection.netty;

import com.google.common.collect.Maps;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/7/20 23:25
 **/
public class ConnectionContext {

    /**
     * Connection
     */
    private ChannelHandlerContext channelHandlerContext;

    /**
     * if db is not null, this context may use db;
     */
    private String db;

    /**
     * Store the property
     */
    private Map<String, String> properties = Maps.newHashMap();
    /**
     * Store the current query string
     */
    private ThreadLocal<String> queryString = new ThreadLocal<>();

    public ConnectionContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void write(ByteBuf byteBuf) {
        channelHandlerContext.writeAndFlush(byteBuf);
        if (ReferenceCountUtil.refCnt(byteBuf) > 0) {
            ReferenceCountUtil.release(byteBuf);
        }
    }

    public void write(MysqlPackage result) {
        final ByteBuf byteBuf = PackageUtils.packageToBuf(result);
        write(byteBuf);
    }

    public void setQueryString(String sql) {
        queryString.set(sql);
    }

    public String getQueryString() {
        return queryString.get();
    }

}
