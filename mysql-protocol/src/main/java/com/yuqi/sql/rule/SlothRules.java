package com.yuqi.sql.rule;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.rel.rules.CoreRules;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 12/7/20 20:57
 **/
public class SlothRules {

    public static final List<RelOptRule> CONVERTER_RULE = ImmutableList.of(
            SlothProjectConvertRule.INSTANCE,
            SlothValueConvertRule.INSTANCE

    );

    public static final List<RelOptRule> CONSTANT_REDUCTION_RULES = ImmutableList.of(
            CoreRules.PROJECT_REDUCE_EXPRESSIONS,
            CoreRules.FILTER_REDUCE_EXPRESSIONS,
            CoreRules.CALC_REDUCE_EXPRESSIONS,
            CoreRules.WINDOW_REDUCE_EXPRESSIONS,
            CoreRules.JOIN_REDUCE_EXPRESSIONS,
            CoreRules.FILTER_VALUES_MERGE,
            CoreRules.PROJECT_FILTER_VALUES_MERGE,
            CoreRules.PROJECT_VALUES_MERGE,
            CoreRules.AGGREGATE_VALUES);
}
