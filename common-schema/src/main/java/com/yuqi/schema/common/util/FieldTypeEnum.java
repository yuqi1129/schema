package com.yuqi.schema.common.util;

import com.google.common.collect.ImmutableMap;
import org.apache.calcite.linq4j.tree.Primitive;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 19:39
 **/
public enum FieldTypeEnum {
    /**
     *
     */
    STRING(null, String.class),

    /**
     *
     */
    BOOLEAN(Primitive.BOOLEAN),

    /**
     *
     */
    BYTE(Primitive.BYTE),
    /**
     *
     */
    CHAR(Primitive.CHAR),

    /**
     *
     */
    SHORT(Primitive.SHORT),

    /**
     *
     */
    INT(Primitive.INT),

    /**
     *
     */
    LONG(Primitive.LONG),

    /**
     *
     */
    FLOAT(Primitive.FLOAT),

    /**
     *
     */
    DOUBLE(Primitive.DOUBLE),

    /**
     *
     */
    DATE(null, java.sql.Date.class),

    /**
     *
     */
    TIME(null, java.sql.Time.class),

    /**
     *
     */
    TIMESTAMP(null, java.sql.Timestamp.class);

    private final Primitive primitive;
    private final Class clazz;

    private static final Map<String, FieldTypeEnum> MAP;


    FieldTypeEnum(Primitive primitive) {
        this(primitive, primitive.boxClass);
    }

    FieldTypeEnum(Primitive primitive, Class clazz) {
        this.primitive = primitive;
        this.clazz = clazz;
    }


    static {
        ImmutableMap.Builder<String, FieldTypeEnum> builder =
                ImmutableMap.builder();
        for (FieldTypeEnum value : values()) {
            builder.put(value.clazz.getSimpleName(), value);

            if (value.primitive != null) {
                builder.put(value.primitive.primitiveName, value);
            }
        }
        MAP = builder.build();
    }

    public static Class<?> getByTypeName(String type) {
        return MAP.get(type).clazz;
    }

    public static FieldTypeEnum getByType(String type) {
        return MAP.get(type);
    }
}
