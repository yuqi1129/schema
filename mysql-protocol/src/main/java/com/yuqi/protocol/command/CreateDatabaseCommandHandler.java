package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.request.CreateDbPackage;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 26/7/20 17:02
 **/
public class CreateDatabaseCommandHandler extends AbstractCommandHandler {

    private CreateDbPackage createDbPackage;

    public CreateDatabaseCommandHandler(ConnectionContext connectionContext, CreateDbPackage createDbPackage) {
        super(connectionContext);
        this.createDbPackage = createDbPackage;
    }

    @Override
    public void execute() {

    }
}
