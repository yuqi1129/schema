package com.yuqi.protocol.pkg.response;

import com.yuqi.protocol.pkg.AbstractPackage;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 6/7/20 20:57
 */
@AllArgsConstructor
@NoArgsConstructor
public class ColumnCountPackage extends AbstractPackage {

    private int columnCount;

    @Override
    public void read(ByteBuf byteBuf) {
        IOUtils.readLengthEncodedInteger(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {
        IOUtils.writeLengthEncodedInteger(columnCount, byteBuf);
    }
}
