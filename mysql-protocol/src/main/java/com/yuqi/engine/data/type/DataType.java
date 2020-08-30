package com.yuqi.engine.data.type;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/8/20 11:28
 **/


import com.yuqi.engine.data.value.Value;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public abstract class DataType<T> implements Comparable<DataType<T>>, Streamer<T>, Comparator<T> {

    public enum Precedence {
        NOT_SUPPORTED,
        UNDEFINED,
        LITERAL,
        STRING,
        BYTE,
        BOOLEAN,
        SHORT,
        INTEGER,
        INTERVAL,
        TIMESTAMP_WITH_TIME_ZONE,
        DATE,
        TIMESTAMP,
        LONG,
        FLOAT,
        DOUBLE,
        ARRAY,
        SET,
        TABLE,
        GEO_POINT,
        OBJECT,
        UNCHECKED_OBJECT,
        GEO_SHAPE,
        CUSTOM
    }

    public abstract int id();

    public abstract Precedence precedence();

    public abstract String getName();

    public Streamer<T> streamer() {
        return this;
    }

    public abstract T value(Object value);

    List<DataType<?>> getTypeParameters() {
        return Collections.emptyList();
    }

    public boolean precedes(DataType other) {
        return this.precedence().ordinal() > other.precedence().ordinal();
    }

    public boolean isConvertableTo(DataType<?> other) {
        if (this.equals(other)) {
            return true;
        }
        Set<Integer> possibleConversions = DataTypes.ALLOWED_CONVERSIONS.get(id());
        //noinspection SimplifiableIfStatement
        if (possibleConversions == null) {
            return false;
        }
        return possibleConversions.contains(other.id());
    }

    @Override
    public int hashCode() {
        return id();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof DataType)) {
            return false;
        }

        DataType<?> that = (DataType<?>) o;
        return (id() == that.id());
    }

    @Override
    public int compareTo(DataType o) {
        return Integer.compare(id(), o.id());
    }

    @Override
    public String toString() {
        return getName();
    }

    public Value createByType(Object o) {
        return new Value(o, this);
    }
}