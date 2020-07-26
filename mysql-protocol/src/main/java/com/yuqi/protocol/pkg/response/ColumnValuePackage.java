package com.yuqi.protocol.pkg.response;

import com.yuqi.protocol.pkg.AbstractPackage;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 6/7/20 21:19
 **/
public class ColumnValuePackage extends AbstractPackage {

    //all value encode with LengthEncodedString
    private List<String> texts;

    public ColumnValuePackage(List<String> texts) {
        this.texts = texts;
    }

    @Override
    public void write(ByteBuf byteBuf) {
        texts.forEach(t -> IOUtils.writeLengthEncodedString(byteBuf, t));
    }
}
