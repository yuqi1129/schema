package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.request.UsePackage;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 26/7/20 17:18
 **/
public class UseDatabaseCommandHandler extends AbstractCommandHandler {
    private UsePackage usePackage;

    public UseDatabaseCommandHandler(ConnectionContext connectionContext, UsePackage usePackage) {
        super(connectionContext);
        this.usePackage = usePackage;
    }


    @Override
    public void execute() {
        //查找当前是否有db, 如果有，connectionContext中设置db
        //如果没有，报错返回
    }
}
