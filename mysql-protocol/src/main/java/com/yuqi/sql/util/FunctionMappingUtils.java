package com.yuqi.sql.util;

import com.google.common.collect.Maps;
import com.yuqi.engine.data.func.AbsFunction;
import com.yuqi.engine.data.func.ArithmeticFunction;
import com.yuqi.engine.data.func.CompareFunction;
import com.yuqi.engine.data.func.LogicalFunction;
import com.yuqi.engine.data.func.Scalar;
import com.yuqi.engine.data.func.UnaryMinusFunction;

import java.util.Map;

import static org.apache.calcite.sql.fun.SqlStdOperatorTable.ABS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.AND;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.DIVIDE;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.EQUALS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.GREATER_THAN;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.GREATER_THAN_OR_EQUAL;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.LESS_THAN;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.LESS_THAN_OR_EQUAL;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.MINUS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.MULTIPLY;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.NOT_EQUALS;
import static org.apache.calcite.sql.fun.SqlStdOperatorTable.OR;
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

        //compuate
        NAME_TO_SCLAR_MAP.put(ABS.getName(), AbsFunction.INSTANCE);
        NAME_TO_SCLAR_MAP.put(UNARY_MINUS.getName(), UnaryMinusFunction.INSTANCE);
        NAME_TO_SCLAR_MAP.put(PLUS.getName(), ArithmeticFunction.PLUS);
        NAME_TO_SCLAR_MAP.put(MINUS.getName(), ArithmeticFunction.MINUS);
        NAME_TO_SCLAR_MAP.put(DIVIDE.getName(), ArithmeticFunction.DIVIDE);
        NAME_TO_SCLAR_MAP.put(MULTIPLY.getName(), ArithmeticFunction.MULTIPLY);



        //compare
        NAME_TO_SCLAR_MAP.put(LESS_THAN.getName(), CompareFunction.LESS_THAN);
        NAME_TO_SCLAR_MAP.put(LESS_THAN_OR_EQUAL.getName(), CompareFunction.LESS_THAN_OR_EQUAL);
        NAME_TO_SCLAR_MAP.put(GREATER_THAN.getName(), CompareFunction.GREATER_THAN);
        NAME_TO_SCLAR_MAP.put(GREATER_THAN_OR_EQUAL.getName(), CompareFunction.GREATER_THAN_OR_EQUAL);
        NAME_TO_SCLAR_MAP.put(EQUALS.getName(), CompareFunction.EQUALS);
        NAME_TO_SCLAR_MAP.put(NOT_EQUALS.getName(), CompareFunction.NOT_EQUALS);


        //LOGICLA
        NAME_TO_SCLAR_MAP.put(AND.getName(), LogicalFunction.LOGICAL_AND);
        NAME_TO_SCLAR_MAP.put(OR.getName(), LogicalFunction.LOGICAL_OR);

    }

    public static final Scalar getFunctionByName(String name) {
        return NAME_TO_SCLAR_MAP.get(name);
    }
}
