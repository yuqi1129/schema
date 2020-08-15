package com.yuqi.engine.data.value;


import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.type.DataTypes;

import java.math.BigDecimal;
import java.util.Objects;

import static com.yuqi.engine.data.type.DataTypes.BOOLEAN;
import static com.yuqi.engine.data.type.DataTypes.BYTE;
import static com.yuqi.engine.data.type.DataTypes.DOUBLE;
import static com.yuqi.engine.data.type.DataTypes.FLOAT;
import static com.yuqi.engine.data.type.DataTypes.INTEGER;
import static com.yuqi.engine.data.type.DataTypes.LONG;
import static com.yuqi.engine.data.type.DataTypes.SHORT;
import static com.yuqi.engine.data.type.DataTypes.STRING;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/8/20 16:29
 **/
public class Value implements Comparable<Value> {

    protected Object value;

    //最终的控制输出格式是由dataType确定的，可能value与DataType不一致
    //比如说 value = Integer, DataType为Long, 最终需要
    protected DataType dataType;

    public Value(Object value) {
        this.value = value;
    }

    public Value(Object value, DataType dataType) {
        this.value = value;
        this.dataType = dataType;
    }

    public DataType<?> getType() {
        return dataType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isNull() {
        return null == value;
    }

    public Integer intValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (value instanceof Number) {
            //TODO 溢出判断
            return Integer.valueOf(value.toString());
        }

        if (cl == Boolean.class) {
            return ((Boolean) value) ? 1 : 0;
        }

        if (cl == String.class) {
            return 0;
        }

        return 0;
    }

    public Byte byteValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (value instanceof Number) {
            //TODO 溢出判断
            return Byte.valueOf(value.toString());
        }

        if (cl == Boolean.class) {
            return ((Boolean) value) ? (byte) 1 : (byte) 0;
        }

        if (cl == String.class) {
            return (byte) 0;
        }

        return (byte) 0;
    }

    public Short shortValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (value instanceof Number) {
            //TODO 溢出判断
            return Short.valueOf(value.toString());
        }

        if (cl == Boolean.class) {
            return ((Boolean) value) ? (short) 1 : (short) 0;
        }

        if (cl == String.class) {
            return (short) 0;
        }

        return (short) 0;
    }

    public Long longValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (value instanceof Number) {
            //TODO 溢出判断
            return Long.valueOf(value.toString());
        }

        if (cl == Boolean.class) {
            return ((Boolean) value) ? 1L : 0L;
        }

        if (cl == String.class) {
            return 0L;
        }

        return 0L;
    }

    public Float floatValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (value instanceof Number) {
            //TODO 溢出判断
            return Float.valueOf(value.toString());
        }

        if (cl == Boolean.class) {
            return ((Boolean) value) ? 1f : 0f;
        }

        if (cl == String.class) {
            return 0f;
        }

        return 0f;
    }

    public Double doubleValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (value instanceof Number) {
            //TODO 溢出判断
            return Double.valueOf(value.toString());
        }

        if (cl == Boolean.class) {
            return ((Boolean) value) ? (double) 1 : (double) 0;
        }

        if (cl == String.class) {
            return (double) 0;
        }

        return (double) 0;
    }

    public Boolean booleanValue() {

        if (isNull()) {
            return null;
        }

        final Class cl = value.getClass();

        if (cl == Boolean.class) {
            return (Boolean) value;
        }

        if (value instanceof Number) {
            return BigDecimal.ZERO.equals(new BigDecimal(value.toString()));
        }

        if (cl == String.class) {
            return false;
        }

        return false;
    }

    public String stringValue() {

        if (isNull()) {
            return null;
        }

        if (value instanceof String) {
            return (String) value;
        }

        return value.toString();
    }

    public Value copy() {
        return new Value(value, dataType);
    }

    public Object getValueByType() {
        DataType dataType = getType();

        if (null == value) {
            return value;
        }

        if (dataType == INTEGER) {
            return intValue();
        } else if (dataType == BYTE) {
            return byteValue();
        } else if (dataType == SHORT) {
            return shortValue();
        } else if (dataType == LONG) {
            return longValue();
        } else if (dataType == DOUBLE) {
            return doubleValue();
        } else if (dataType == FLOAT) {
            return floatValue();
        } else if (dataType == BOOLEAN) {
            return booleanValue();
        } else {
            return stringValue();
        }
    }

    @Override
    public int compareTo(Value o) {
        if (o.isNull()) {
            return 1;
        }

        //TODO 暂时不考虑boolean类型
        if (DataTypes.INTEGER_TYPES.contains(o.getType()) && DataTypes.INTEGER_TYPES.contains(this.getType())) {
            return Long.compare(longValue(), o.longValue());
        }

        if (STRING == o.dataType && STRING == this.dataType) {
            return this.stringValue().compareTo(o.stringValue());
        }


        return BigDecimal.valueOf(doubleValue()).compareTo(BigDecimal.valueOf(o.doubleValue()));
    }

    public Value compare(Value o) {
       return null;
    }

    public static Value ofBooleanTrue() {
        return new Value(true, BOOLEAN);
    }

    public static Value ofBooleanFalse() {
        return new Value(false, BOOLEAN);
    }

    public static Value ofBooean(boolean b) {
        return new Value(b, BOOLEAN);
    }

    @Override
    public boolean equals(Object o) {

        if (o.getClass() != Value.class) {
            return false;
        }

        Value v = (Value) o;

        if (isNull()) {
            return v.isNull();
        }

        return this.getValueByType().equals(v.getValueByType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, dataType);
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
