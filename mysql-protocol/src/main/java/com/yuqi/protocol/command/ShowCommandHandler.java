package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.request.ShowPackage;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 22:05
 **/
public class ShowCommandHandler extends AbstractCommandHandler {

    private ShowPackage showPackage;

    public ShowCommandHandler(ConnectionContext connectionContext, ShowPackage showPackage) {
        super(connectionContext);
        this.showPackage = showPackage;
    }

    @Override
    public void execute() {
        final String commandString = showPackage.getCommand();

        //经SQL parser 一把啊，不然的话判断起来比较麻烦
    }
}
