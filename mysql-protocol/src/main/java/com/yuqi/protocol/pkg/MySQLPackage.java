package com.yuqi.protocol.pkg;

import com.yuqi.protocol.io.PackageReaderAndWriter;
import com.yuqi.protocol.utils.IOUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 21:28
 **/
@Data
@NoArgsConstructor
public class MySQLPackage implements PackageReaderAndWriter {
    /**
     * Length of Message body
     */
    private int lengthOfMessage;

    /**
     * Sequence number
     */
    private byte seqNumber;

    /**
     *
     */
    private AbstractPackage abstractPackage;


    public MySQLPackage(AbstractPackage abstractPackage) {
        this.abstractPackage = abstractPackage;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        //read and then
        this.lengthOfMessage = IOUtils.readInteger(byteBuf, 3);
        this.seqNumber = IOUtils.readByte(byteBuf);
        abstractPackage.read(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {

        ByteBuf tmp = PooledByteBufAllocator.DEFAULT.buffer(128);
        abstractPackage.write(tmp);

        this.lengthOfMessage = tmp.readableBytes();
        IOUtils.writeInteger3(lengthOfMessage, byteBuf);
        IOUtils.writeByte(seqNumber, byteBuf);
        byte[] bytes = new byte[tmp.readableBytes()];
        tmp.readBytes(bytes);

        //change from writeBytes -->  writeBytesWithoutEndFlag
        IOUtils.writeBytesWithoutEndFlag(bytes, byteBuf);
    }
}
