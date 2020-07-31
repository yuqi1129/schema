package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.ConnectionContext;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 19:13
 **/
public class UseDbCommandHandler extends AbstractCommandHandler {

    public UseDbCommandHandler(ConnectionContext connectionContext) {
        super(connectionContext);
    }

    @Override
    public void execute() {

    }
}
