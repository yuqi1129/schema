package com.yuqi.engine.data.func;

import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 16:23
 **/
public abstract class Scalar {

    protected int arglength;

    public Scalar(int arglength) {
        this.arglength = arglength;
    }

    public abstract Value evaluate(List<Value> args);
}
