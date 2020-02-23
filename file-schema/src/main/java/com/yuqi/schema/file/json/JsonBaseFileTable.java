package com.yuqi.schema.file.json;

import com.yuqi.schema.file.AbstractFileReader;
import com.yuqi.schema.file.BaseFileTable;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 16:17
 **/
public class JsonBaseFileTable extends BaseFileTable {
    public JsonBaseFileTable(AbstractFileReader fileReader) {
        super(fileReader);
    }

    @Override
    public String toString() {
        return "JsonFileTable";
    }

    @Override
    public Statistic getStatistic() {
        return Statistics.UNKNOWN;
    }



}
