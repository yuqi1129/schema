package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import static com.yuqi.protocol.constants.CommandTypeConstants.COM_CREATE_DB;
import static com.yuqi.protocol.constants.CommandTypeConstants.COM_QUERY;
import static com.yuqi.protocol.constants.CommandTypeConstants.COM_USE_DB;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 20:29
 **/
@Data
public class Command extends AbstractReaderAndWriter {
    private byte commandType;

    private AbstractReaderAndWriter abstractReaderAndWriterPackage;

    @Override
    public void read(ByteBuf byteBuf) {
        this.commandType = IOUtils.readByte(byteBuf);

        switch (commandType) {
            case COM_QUERY:
                abstractReaderAndWriterPackage = new Query();
                break;
            case COM_CREATE_DB:
                abstractReaderAndWriterPackage = new CreateDb();
                break;
            case COM_USE_DB:
                abstractReaderAndWriterPackage = new Use();
                break;
            default:
                abstractReaderAndWriterPackage = new Show();
        }

        abstractReaderAndWriterPackage.read(byteBuf);
    }
}
