package com.yuqi.engine.data.value;

import com.yuqi.engine.data.type.DataType;

import static com.yuqi.engine.data.type.DataTypes.INTEGER;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 17:12
 **/
public class IntValue extends Value {

    public static final Value INT_NULL = new IntValue(null);

    public IntValue(Object value) {
        super(value);
    }

    @Override
    public DataType<?> getType() {
        return INTEGER;
    }

    @Override
    public Object getValue() {
        return intValue();
    }

    @Override
    public Integer intValue() {
        return (Integer) value;
    }

    @Override
    public Byte byteValue() {
        //check overflow
        return (Byte) value;
    }

    @Override
    public Short shortValue() {
        return (Short) value;
    }

    @Override
    public Long longValue() {
        return (Long) value;
    }

    @Override
    public Float floatValue() {
        return Float.valueOf(value.toString());
    }

    @Override
    public Double doubleValue() {
        return Double.valueOf(value.toString());
    }

    @Override
    public Boolean booleanValue() {
        return (Integer) value == 0;
    }

    @Override
    public String stringValue() {
        return value.toString();
    }

    @Override
    public IntValue copy() {
        return new IntValue(value);
    }
}
