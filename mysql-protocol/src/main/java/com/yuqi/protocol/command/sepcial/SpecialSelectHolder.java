package com.yuqi.protocol.command.sepcial;

import com.google.common.collect.Maps;
import com.yuqi.protocol.command.sqlnode.Handler;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 18:44
 **/
public class SpecialSelectHolder {
    public static final Map<String, Handler> SPECIAL_HANDLER = Maps.newHashMap();

    static {
        SPECIAL_HANDLER.put("SELECT `DATABASE`()", CurrentDatabaseHandler.INSTANCE);
    }
}
