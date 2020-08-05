package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 14:15
 **/
public class IntegerType extends DataType<Integer> implements FixedWidthType {
    public static final IntegerType INSTANCE = new IntegerType();

    public static final int ID = 9;

    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.INTEGER;
    }

    @Override
    public String getName() {
        return "integer";
    }

    @Override
    public Integer value(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }

        long longVal = ((Number) value).longValue();
        if (longVal < Integer.MIN_VALUE || Integer.MAX_VALUE < longVal) {
            throw new IllegalArgumentException("integer value out of range: " + longVal);
        }

        return ((Number) value).intValue();
    }

    @Override
    public Integer readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {
        //todo
    }

    @Override
    public int compare(Integer o1, Integer o2) {

        //TODO NPE will throw
        return Integer.compare(o1, o2);
    }

    @Override
    public int fixedSize() {
        return 4 * 8;
    }
}
