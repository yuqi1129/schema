package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 14:38
 **/
public class LongType extends DataType<Long> implements FixedWidthType {

    public static final LongType INSTANCE = new LongType();
    public static final int ID = 10;

    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.LONG;
    }

    @Override
    public String getName() {
        return "bigint";
    }

    @Override
    public Long value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof String) {
            return Long.valueOf((String) value);
        }
        return ((Number) value).longValue();
    }

    @Override
    public int fixedSize() {
        return 8 * 8;
    }

    @Override
    public Long readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {

    }

    @Override
    public int compare(Long o1, Long o2) {
        return Long.compare(o1, o2);
    }
}
