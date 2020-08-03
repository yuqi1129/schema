package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;

import java.lang.reflect.ParameterizedType;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 16:53
 **/
public interface Handler<T> {

    void handle(ConnectionContext connectionContext, T type);

    /**
     * Get Class of T
     * @return
     */
    default Class<T> getType() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return tClass;
    }
}
