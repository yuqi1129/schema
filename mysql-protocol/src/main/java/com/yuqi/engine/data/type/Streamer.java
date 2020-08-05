package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 11:25
 **/
public interface Streamer<T> {
    T readValueFrom(InputStream in) throws IOException;

    void writeValueTo(OutputStream out) throws IOException;
}
