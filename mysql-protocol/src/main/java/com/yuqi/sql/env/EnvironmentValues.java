package com.yuqi.sql.env;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/9/20 15:52
 **/
public class EnvironmentValues {
    public static final Map<String, String> GLOBAL_ENVIRONMENT = Maps.newHashMap();

    static {
        GLOBAL_ENVIRONMENT.put("version_comment", "Create by yuqi");
    }
}
