package com.yuqi.sql.util;

import com.google.common.collect.Maps;
import com.yuqi.engine.data.func.AbsFunction;
import com.yuqi.engine.data.func.Scalar;

import java.util.Map;

import static org.apache.calcite.sql.fun.SqlStdOperatorTable.ABS;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 20:01
 **/
public class FunctionMappingUtils {

    public static final Map<String, Scalar> NAME_TO_SCLAR_MAP = Maps.newHashMap();

    static {
        NAME_TO_SCLAR_MAP.put(ABS.getName(), AbsFunction.INSTANCE);
    }

    public static final Scalar getFunctionByName(String name) {
        return NAME_TO_SCLAR_MAP.get(name);
    }
}
