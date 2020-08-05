package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 14:34
 **/
public class DoubleType extends DataType<Double> implements FixedWidthType {


    public static final DoubleType INSTANCE = new DoubleType();
    public static final int ID = 6;

    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.DOUBLE;
    }

    @Override
    public String getName() {
        return "double";
    }

    @Override
    public Double value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        return ((Number) value).doubleValue();
    }

    @Override
    public int fixedSize() {
        return 8 * 8;
    }

    @Override
    public Double readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {

    }

    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }
}
