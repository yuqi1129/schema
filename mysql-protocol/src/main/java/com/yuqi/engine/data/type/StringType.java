package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 14:28
 **/
public class StringType extends DataType<String> {
    public static final int ID = 4;
    public static final StringType INSTANCE = new StringType();

    private static final String T = "t";
    private static final String F = "f";

    @Override
    public int id() {
        return ID;
    }

    @Override
    public Precedence precedence() {
        return Precedence.STRING;
    }

    @Override
    public String getName() {
        return "text";
    }


    @Override
    public String value(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }

        if (value instanceof Boolean) {
            if ((boolean) value) {
                return T;
            } else {
                return F;
            }
        }

        return value.toString();
    }

    @Override
    public int compare(String val1, String val2) {
        return val1.compareTo(val2);
    }

    @Override
    public String readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {

    }
}
