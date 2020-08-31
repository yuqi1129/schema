package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 30/8/20 21:03
 **/
public class DateType extends DataType<Long> implements FixedWidthType {

    public static final DateType INSTANCE = new DateType();
    public static final int ID = 11;
    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.DATE;
    }

    @Override
    public String getName() {
        return "date";
    }

    @Override
    public Long value(Object value) {
        return null;
    }

    @Override
    public int fixedSize() {
        return 0;
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
        return o1.compareTo(o2);
    }
}
