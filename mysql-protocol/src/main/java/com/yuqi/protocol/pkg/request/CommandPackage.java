package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractPackage;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import static com.yuqi.protocol.constants.CommandTypeConstants.COM_QUERY;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 20:29
 **/
@Data
public class CommandPackage extends AbstractPackage {
    private byte commandType;

    private AbstractPackage abstractPackage;

    @Override
    public void read(ByteBuf byteBuf) {
        this.commandType = IOUtils.readByte(byteBuf);

        switch (commandType) {
            case COM_QUERY:
                abstractPackage = new QueryPackage();
                break;

            default:
                abstractPackage = new ShowPackage();
        }

        abstractPackage.read(byteBuf);
    }
}
