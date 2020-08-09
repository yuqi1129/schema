package com.yuqi.protocol.pkg;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 15:57
 **/

@Builder
@Getter
public class ResultSetHolder {
    private List<Integer> columnType;

    //may be we can use pool data array to avoid frequent allocated space
    private List<List<String>> data;
    private String[] columnName;

    private String schema;

    private String table;
}
