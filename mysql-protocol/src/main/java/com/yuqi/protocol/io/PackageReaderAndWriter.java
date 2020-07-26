package com.yuqi.protocol.io;

import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/6/20 21:15
 **/
public interface PackageReaderAndWriter {
    /**
     * Read content from {@link ByteBuf}
     * @param byteBuf
     */
    default void read(ByteBuf byteBuf) {
        //TODO
    }


    /**
     * Write content to BytBuf
     * @param byteBuf
     */
    default void write(ByteBuf byteBuf) {
        //TODO
    }
}
