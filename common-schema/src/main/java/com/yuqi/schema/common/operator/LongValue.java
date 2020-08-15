package com.yuqi.schema.common.operator;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:55
 **/
public class LongValue implements Value<Long> {

    private Long value;

    @Override
    public Long getLong() {
        return null;
    }

    @Override
    public Integer getInt() {
        return null;
    }

    @Override
    public Long value() {
        return value;
    }
}
