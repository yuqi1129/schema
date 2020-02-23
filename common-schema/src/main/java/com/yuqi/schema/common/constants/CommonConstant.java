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

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public static final String CALCITE_URL = "jdbc:calcite:";

    public static final String DERBY_URL = "jdbc:derby:memory:xxx;create=true";
    public static final String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String SCHEMA = "schema";
}
