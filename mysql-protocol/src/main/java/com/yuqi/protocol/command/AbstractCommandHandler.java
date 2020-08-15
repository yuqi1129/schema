package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.ConnectionContext;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/7/20 23:36
 **/
public abstract class AbstractCommandHandler implements CommandHandler {
    protected ConnectionContext connectionContext;

    public AbstractCommandHandler(ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }
}
