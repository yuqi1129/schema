package com.yuqi.sql.rule;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.rel.rules.JoinPushThroughJoinRule;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 12/7/20 20:57
 **/
public class SlothRules {

    public static final List<RelOptRule> CONVERTER_RULE = ImmutableList.of(
            SlothProjectConverterRule.INSTANCE,
            SlothValueConverterRule.INSTANCE,
            SlothJoinConverterRule.INSTANCE,
            SlothFilterConverterRule.INSTANCE,
            SlothSortConverterRule.INSTANCE,
            SlothAggregateConverterRule.INSTANCE,
            SlothUnionConverterRule.INSTANCE,
            SlothTableScanConverterRule.INSTANCE
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

    public static final List<RelOptRule> BASE_RULES = ImmutableList.of(
            CoreRules.AGGREGATE_STAR_TABLE,
            CoreRules.AGGREGATE_PROJECT_STAR_TABLE,
            CalciteSystemProperty.COMMUTE.value()
                    ? CoreRules.JOIN_ASSOCIATE
                    : CoreRules.PROJECT_MERGE,
            CoreRules.FILTER_SCAN,
            CoreRules.PROJECT_FILTER_TRANSPOSE,
            CoreRules.FILTER_PROJECT_TRANSPOSE,
            CoreRules.FILTER_INTO_JOIN,
            CoreRules.JOIN_PUSH_EXPRESSIONS,
            CoreRules.AGGREGATE_EXPAND_DISTINCT_AGGREGATES,
            CoreRules.AGGREGATE_CASE_TO_FILTER,
            CoreRules.AGGREGATE_REDUCE_FUNCTIONS,
            CoreRules.FILTER_AGGREGATE_TRANSPOSE,
            CoreRules.PROJECT_WINDOW_TRANSPOSE,
            CoreRules.MATCH,
            CoreRules.JOIN_COMMUTE,
            JoinPushThroughJoinRule.RIGHT,
            JoinPushThroughJoinRule.LEFT,
            CoreRules.SORT_PROJECT_TRANSPOSE,
            CoreRules.SORT_JOIN_TRANSPOSE,
            CoreRules.SORT_REMOVE_CONSTANT_KEYS,
            CoreRules.SORT_UNION_TRANSPOSE,
            CoreRules.EXCHANGE_REMOVE_CONSTANT_KEYS,
            CoreRules.SORT_EXCHANGE_REMOVE_CONSTANT_KEYS);
}
