package com.yuqi.schema.common.operator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/5/20 21:28
 **/
public class FileTable implements Table {
    //

    @Override
    public List<Value> query() {
        //存储引擎查询，可能要用到lucece等，也可以先不用管
        return null;
    }
}
