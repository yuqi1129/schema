package com.yuqi.schema.common.operator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:54
 **/
public class IntegerValue implements Value<Integer> {
    private Integer value;

    public IntegerValue(Integer value) {
        this.value = value;
    }

    @Override
    public Long getLong() {
        return null;
    }

    @Override
    public Integer getInt() {
        return null;
    }

    @Override
    public Integer value() {
        return value;
    }
}
