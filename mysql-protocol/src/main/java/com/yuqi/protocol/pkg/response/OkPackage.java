package com.yuqi.protocol.pkg.response;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Data;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/7/20 11:14
 **/
@Data
@Builder
public class OkPackage extends AbstractReaderAndWriter {

    /**
     * 0x00 is ok
     * 0xfe is EOF
     */
    private byte header;

    //add later
    private int affectedRows;

    //add later
    private int lastInsertId;

    private int serverStatus;

    //add later
    private int numberOfWarning;

    private String info;

    @Override
    public void write(ByteBuf byteBuf) {
        IOUtils.writeByte(header, byteBuf);
        IOUtils.writeInteger(affectedRows, byteBuf, 1);
        IOUtils.writeInteger(lastInsertId, byteBuf, 1);
        IOUtils.writeInteger(serverStatus, byteBuf, 2);
        IOUtils.writeInteger(numberOfWarning, byteBuf, 2);

        if (info != null) {
            IOUtils.writeLengthEncodedString(byteBuf, info);
        }
    }
}
