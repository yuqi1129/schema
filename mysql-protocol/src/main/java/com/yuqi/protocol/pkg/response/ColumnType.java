package com.yuqi.protocol.pkg.response;

import com.yuqi.protocol.pkg.AbstractReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import lombok.Builder;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 6/7/20 20:59
 **/
@Builder
public class ColumnType extends AbstractReaderAndWriter {

    private String catalog;

    private String schema;

    private String table;

    private String orgTable;

    private String name;

    private String originalName;

    /**
     * alwayas 0x0c
     */
    private byte filler;
    //fix 2 byte
    private int charSet;

    //column max length
    private int columnLength;

    private byte columnType;

    //always 00 00
    private int flags;

    /**
     * 0x00 for integer and static string
     * 0x01 for dynamic strings, double, float
     * 0x00 to 051 for decimals
     */
    private byte dicimals;

    @Override
    public void write(ByteBuf byteBuf) {
        IOUtils.writeLengthEncodedString(byteBuf, catalog);
        IOUtils.writeLengthEncodedString(byteBuf, schema);
        IOUtils.writeLengthEncodedString(byteBuf, table);
        IOUtils.writeLengthEncodedString(byteBuf, orgTable);
        IOUtils.writeLengthEncodedString(byteBuf, name);
        IOUtils.writeLengthEncodedString(byteBuf, originalName);

        IOUtils.writeByte(filler, byteBuf);
        IOUtils.writeInteger(charSet, byteBuf, 2);
        IOUtils.writeInteger(columnLength, byteBuf, 4);
        IOUtils.writeByte(columnType, byteBuf);
        IOUtils.writeInteger(flags, byteBuf, 2);
        IOUtils.writeByte(dicimals, byteBuf);

        //the following
        IOUtils.writeByte((byte) 0x00, byteBuf);
        IOUtils.writeByte((byte) 0x00, byteBuf);
    }
}
