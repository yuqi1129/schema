package com.yuqi.sql.util;

import com.google.common.collect.Maps;
import com.yuqi.engine.data.func.AbsFunction;
import com.yuqi.engine.data.func.ArithmeticFunction;
import com.yuqi.engine.data.func.Scalar;
import com.yuqi.engine.data.func.UnaryMinusFunction;

import java.util.Map;

import static org.apache.calcite.sql.fun.SqlStdOperatorTable.ABS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.DIVIDE;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.MINUS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.MULTIPLY;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.PLUS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.UNARY_MINUS;

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
        NAME_TO_SCLAR_MAP.put(UNARY_MINUS.getName(), UnaryMinusFunction.INSTANCE);
        NAME_TO_SCLAR_MAP.put(PLUS.getName(), ArithmeticFunction.PLUS);
        NAME_TO_SCLAR_MAP.put(MINUS.getName(), ArithmeticFunction.MINUS);
        NAME_TO_SCLAR_MAP.put(DIVIDE.getName(), ArithmeticFunction.DIVIDE);
        NAME_TO_SCLAR_MAP.put(MULTIPLY.getName(), ArithmeticFunction.MULTIPLY);
    }

    public static final Scalar getFunctionByName(String name) {
        return NAME_TO_SCLAR_MAP.get(name);
    }
}
