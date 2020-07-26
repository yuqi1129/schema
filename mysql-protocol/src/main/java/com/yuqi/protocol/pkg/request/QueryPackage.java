package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractPackage;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 20:36
 **/
public class QueryPackage extends AbstractPackage {
    private String query;


    @Override
    public void read(ByteBuf byteBuf) {
        this.query = IOUtils.readString(byteBuf);
    }
}
