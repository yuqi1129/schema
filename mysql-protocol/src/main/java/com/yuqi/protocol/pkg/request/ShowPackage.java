package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractPackage;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 21:20
 **/
public class ShowPackage extends AbstractPackage {
    private String command;

    public String getCommand() {
        return command;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        //todo
    }
}
