package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/8/20 14:29
 **/
public class FloatType extends DataType<Float> implements FixedWidthType {
    public static final FloatType INSTANCE = new FloatType();
    public static final int ID = 7;

    private FloatType() {
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.FLOAT;
    }

    @Override
    public String getName() {
        return "real";
    }

    @Override
    public Float value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof String) {
            return Float.parseFloat((String) value);
        }
        double doubleValue = ((Number) value).doubleValue();
        if (doubleValue < -Float.MAX_VALUE || Float.MAX_VALUE < doubleValue) {
            throw new IllegalArgumentException("float value out of range: " + doubleValue);
        }
        return ((Number) value).floatValue();
    }

    @Override
    public int compare(Float val1, Float val2) {
        return Float.compare(val1, val2);
    }

    @Override
    public int fixedSize() {
        return 4 * 8;
    }

    @Override
    public Float readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {

    }
}
