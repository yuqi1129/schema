package com.yuqi.protocol.command.sepcial;

import com.google.common.collect.Maps;
import com.yuqi.protocol.command.sqlnode.Handler;

import java.util.Map;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 3/8/20 20:11
 **/
public class RawHandlerHolder {

    public static final Map<String, Handler> RAW_HANDLER = Maps.newHashMap();

    public static final String RAW_COMMAND_EXPLAIN = "EXPLAIN";

    static {
        RAW_HANDLER.put(RAW_COMMAND_EXPLAIN, ExplainHandler.INSTANCE);
    }


    public static Handler getRawHandler(String query) {
        final String q = query.toUpperCase();

        final String firstWord = q.split(" ", 2)[0].trim();
        if (Objects.equals(RawHandlerHolder.RAW_COMMAND_EXPLAIN, firstWord)) {
            return RAW_HANDLER.get(RAW_COMMAND_EXPLAIN);
        }

        return null;
    }
}
