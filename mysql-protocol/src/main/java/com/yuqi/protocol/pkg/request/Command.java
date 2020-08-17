package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/7/20 20:29
 **/
@Data
public class Command extends AbstractReaderAndWriter {
    private byte commandType;

    private String command;

    @Override
    public void read(ByteBuf byteBuf) {
        this.commandType = IOUtils.readByte(byteBuf);
        this.command = IOUtils.readEofString(byteBuf);
    }
}
