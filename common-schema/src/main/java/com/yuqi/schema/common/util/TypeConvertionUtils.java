package com.yuqi.schema.common.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 20:03
 **/
public class TypeConvertionUtils {

    private static final  NumberFormat NUMBER_FORMAT =
            NumberFormat.getInstance(Locale.ROOT);

    /** Format for parsing integers. Not thread-safe, but we assume that only
     * one thread uses this converter at a time. */
    private static final NumberFormat INTEGER_FORMAT =
            NumberFormat.getIntegerInstance(Locale.ROOT);

    public static Object toObject(FieldTypeEnum fieldType, String string) {
        if ((string == null) || (string.length() == 0)) {
            return null;
        }

        if (fieldType == null) {
            return string;
        }

        switch (fieldType) {
            default:
            case STRING:
                return string;

            case BOOLEAN:
                return Boolean.parseBoolean(string);

            case BYTE:
                return Byte.parseByte(string);

            case SHORT:
                try {
                    return INTEGER_FORMAT.parse(string).shortValue();
                } catch (ParseException e) {
                    return null;
                }

            case INT:
                try {
                    return INTEGER_FORMAT.parse(string).intValue();
                } catch (ParseException e) {
                    return null;
                }

            case LONG:
                try {
                    return NUMBER_FORMAT.parse(string).longValue();
                } catch (ParseException e) {
                    return null;
                }

            case FLOAT:
                try {
                    return NUMBER_FORMAT.parse(string).floatValue();
                } catch (ParseException e) {
                    return null;
                }

            case DOUBLE:
                try {
                    return NUMBER_FORMAT.parse(string).doubleValue();
                } catch (ParseException e) {
                    return null;
                }

            case DATE:
            case TIME:
            case TIMESTAMP:
                throw new UnsupportedOperationException("unsupport type " + string);
        }
    }
}
