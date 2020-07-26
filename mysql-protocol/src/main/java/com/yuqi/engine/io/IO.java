package com.yuqi.engine.io;

import io.netty.buffer.ByteBuf;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:06
 **/
public interface IO {

    /**
     * write Object to buffer
     * @param byteBuf
     */
    default void write(ByteBuf byteBuf) {
        //TODO
    }

    /**
     * Read object from buffer
     * @param byteBuf
     */
    default void read(ByteBuf byteBuf) {
        //TODO
    }
}
