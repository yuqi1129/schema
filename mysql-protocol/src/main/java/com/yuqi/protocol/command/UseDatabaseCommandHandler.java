package com.yuqi.protocol.command;

import com.yuqi.protocol.connection.netty.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchemaHolder;
import io.netty.buffer.ByteBuf;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.UNKNOWN_DB_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 26/7/20 17:18
 **/
public class UseDatabaseCommandHandler extends AbstractCommandHandler {
    private String command;

    public UseDatabaseCommandHandler(ConnectionContext connectionContext, String command) {
        super(connectionContext);
        this.command = command;
    }

    @Override
    public void execute() {
        if (SlothSchemaHolder.INSTANCE.getAllSchemas().contains(command)) {
            connectionContext.setDb(command);
            MysqlPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(0, 1, 0);
            ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
            connectionContext.write(byteBuf);
        } else {
            final MysqlPackage mysqlPackage = PackageUtils.buildErrPackage(
                    UNKNOWN_DB_NAME.getCode(),
                    String.format(UNKNOWN_DB_NAME.getMessage(), command));
            connectionContext.write(mysqlPackage);
        }
    }
}
