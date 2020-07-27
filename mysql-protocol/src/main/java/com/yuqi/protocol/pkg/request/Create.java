package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 26/7/20 16:59
 **/
public class Create extends AbstractReaderAndWriter {
    private String command;

    public String getCommand() {
        return command;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.command = IOUtils.readString(byteBuf);
    }
}
