package com.yuqi.engine.data.func.agg;

import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 7/8/20 16:36
 **/
public abstract class AbstractAggregation implements Aggregation {

    protected boolean isDistinct;
    protected boolean ignoreNull;

    protected DataType inputType;
    protected DataType resultType;

    protected int index;
    protected List<List<Value>> originDatas;
    protected List<Integer> groupByIndex;

    protected boolean showGroupByColumn = false;

    public AbstractAggregation(boolean isDistinct, boolean ignoreNull, DataType inputType,
                               DataType resultType, int index) {
        this.isDistinct = isDistinct;
        this.ignoreNull = ignoreNull;
        this.inputType = inputType;
        this.resultType = resultType;
        this.index = index;
    }

    public AbstractAggregation(boolean isDistinct, boolean ignoreNull, DataType inputType,
                               DataType resultType, int index, List<Integer> groupByIndex) {
        this.isDistinct = isDistinct;
        this.ignoreNull = ignoreNull;
        this.inputType = inputType;
        this.resultType = resultType;
        this.index = index;
        this.groupByIndex = groupByIndex;
    }

    public void setShowGroupByColumn(boolean showGroupByColumn) {
        this.showGroupByColumn = showGroupByColumn;
    }

    @Override
    public boolean isDistinct() {
        return isDistinct;
    }

    @Override
    public boolean ignoreNulls() {
        return ignoreNull;
    }

    @Override
    public DataType getResultType() {
        return resultType;
    }

    @Override
    public DataType inputType() {
        return inputType;
    }

    public void setOriginDatas(List<List<Value>> originDatas) {
        this.originDatas = originDatas;
    }
}
