package com.yuqi.schema.common.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 20:21
 **/
public class CommonConstant {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String CSV_SUFFIX = ".csv";
    public static final String JSON_SUFFIX = ".json";
    public static final String SCHEMA_SUFFIX = ".schema";

    public static final String DRIVER = "com.mysql.jdbc.Driver";

}
