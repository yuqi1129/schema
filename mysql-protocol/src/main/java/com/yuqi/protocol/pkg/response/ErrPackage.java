package com.yuqi.protocol.pkg.response;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Data;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 17:57
 **/
@Data
@Builder
public class ErrPackage extends AbstractReaderAndWriter {
    private byte header;

    private short errorCode;

    private String errorMessage;


    @Override
    public void write(ByteBuf byteBuf) {
        IOUtils.writeByte(header, byteBuf);
        IOUtils.writeShort(errorCode, byteBuf);
        IOUtils.writeString(errorMessage, byteBuf);
    }
}
