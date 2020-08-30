package com.yuqi.protocol.io;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 30/6/20 21:15
 **/
public interface ReaderAndWriter extends Serializable {
    /**
     * Read content from {@link ByteBuf}
     * @param byteBuf
     */
    default void read(ByteBuf byteBuf) {
        //TODO
    }


    /**
     * Write content to {@link ByteBuf}
     * @param byteBuf
     */
    default void write(ByteBuf byteBuf) {
        //TODO
    }
}
