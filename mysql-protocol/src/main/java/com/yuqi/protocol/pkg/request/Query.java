package com.yuqi.protocol.pkg.request;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 20:36
 **/
public class Query extends AbstractReaderAndWriter {
    private String query;


    public String getQuery() {
        return query;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        this.query = IOUtils.readString(byteBuf);
    }
}
