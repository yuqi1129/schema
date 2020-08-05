package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 14:20
 **/
public class ShortType extends DataType<Short> implements FixedWidthType {
    public static final ShortType INSTANCE = new ShortType();
    public static final int ID = 8;


    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.SHORT;
    }

    @Override
    public String getName() {
        return "short";
    }

    @Override
    public Short value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof String) {
            return Short.valueOf((String) value);
        }
        int intVal = ((Number) value).intValue();
        if (intVal < Short.MIN_VALUE || Short.MAX_VALUE < intVal) {
            throw new IllegalArgumentException("short value out of range: " + intVal);
        }
        return ((Number) value).shortValue();
    }


    @Override
    public int fixedSize() {
        return 2 * 8;
    }

    @Override
    public Short readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {

    }

    @Override
    public int compare(Short o1, Short o2) {
        return Short.compare(o1, o1);
    }
}
