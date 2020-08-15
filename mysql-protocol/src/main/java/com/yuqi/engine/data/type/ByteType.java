package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/8/20 11:57
 **/
public class ByteType extends DataType<Byte> implements FixedWidthType {

    public static final int ID = 2;
    public static final ByteType INSTANCE = new ByteType();

    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.BYTE;
    }

    @Override
    public String getName() {
        return "byte";
    }

    @Override
    public Byte value(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return Byte.parseByte((String) value);
        }

        int val = ((Number) value).intValue();
        if (val < Byte.MIN_VALUE || Byte.MAX_VALUE < val) {
            throw new IllegalArgumentException("byte value out of range: " + val);
        }

        return (byte) val;
    }

    @Override
    public int fixedSize() {
        //why is 16?
        return 8;
    }

    @Override
    public Byte readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {
        //todo
    }

    @Override
    public int compare(Byte o1, Byte o2) {
        return Byte.compare(o1, o2);
    }
}
