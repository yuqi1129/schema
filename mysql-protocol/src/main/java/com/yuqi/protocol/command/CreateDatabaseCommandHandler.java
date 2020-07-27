package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.request.CreateDb;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 26/7/20 17:02
 **/
public class CreateDatabaseCommandHandler extends AbstractCommandHandler {

    private CreateDb createDbPackage;

    public CreateDatabaseCommandHandler(ConnectionContext connectionContext, CreateDb createDbPackage) {
        super(connectionContext);
        this.createDbPackage = createDbPackage;
    }

    @Override
    public void execute() {

    }
}
