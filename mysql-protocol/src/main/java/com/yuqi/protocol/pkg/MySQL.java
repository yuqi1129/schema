package com.yuqi.protocol.pkg;

import com.yuqi.protocol.io.ReaderAndWriter;
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
public class MySQL implements ReaderAndWriter {
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
    private AbstractReaderAndWriter abstractReaderAndWriterPackage;


    public MySQL(AbstractReaderAndWriter abstractReaderAndWriterPackage) {
        this.abstractReaderAndWriterPackage = abstractReaderAndWriterPackage;
    }

    @Override
    public void read(ByteBuf byteBuf) {
        //read and then
        this.lengthOfMessage = IOUtils.readInteger(byteBuf, 3);
        this.seqNumber = IOUtils.readByte(byteBuf);
        abstractReaderAndWriterPackage.read(byteBuf);
    }

    @Override
    public void write(ByteBuf byteBuf) {

        ByteBuf tmp = PooledByteBufAllocator.DEFAULT.buffer(128);
        abstractReaderAndWriterPackage.write(tmp);

        this.lengthOfMessage = tmp.readableBytes();
        IOUtils.writeInteger3(lengthOfMessage, byteBuf);
        IOUtils.writeByte(seqNumber, byteBuf);
        byte[] bytes = new byte[tmp.readableBytes()];
        tmp.readBytes(bytes);

        //change from writeBytes -->  writeBytesWithoutEndFlag
        IOUtils.writeBytesWithoutEndFlag(bytes, byteBuf);
    }
}
