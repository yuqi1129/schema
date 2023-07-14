package com.yuqi.sql.rule;

import com.google.common.collect.ImmutableList;
import com.yuqi.sql.rule.converter.SlothAggregateConverterRule;
import com.yuqi.sql.rule.converter.SlothFilterConverterRule;
import com.yuqi.sql.rule.converter.SlothJoinConverterRule;
import com.yuqi.sql.rule.converter.SlothProjectConverterRule;
import com.yuqi.sql.rule.converter.SlothSortConverterRule;
import com.yuqi.sql.rule.converter.SlothTableScanConverterRule;
import com.yuqi.sql.rule.converter.SlothUnionConverterRule;
import com.yuqi.sql.rule.converter.SlothValueConverterRule;
import com.yuqi.sql.rule.rbo.FilterOrProjectPushDownToTableScanRule;
import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.volcano.AbstractConverter;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.rel.rules.DateRangeRules;
import org.apache.calcite.rel.rules.PruneEmptyRules;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
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


    public static final List<RelOptRule> AFTER_CBO_RULES = ImmutableList.of(
            FilterOrProjectPushDownToTableScanRule.FILTER_PUSH_DOWN_INSTANCE,
            FilterOrProjectPushDownToTableScanRule.PROJECT_PUSH_DOWN_INSTANCE
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
            //CoreRules.JOIN_COMMUTE,
//            JoinPushThroughJoinRule.RIGHT,
//            JoinPushThroughJoinRule.LEFT,
            CoreRules.SORT_PROJECT_TRANSPOSE,
            CoreRules.SORT_JOIN_TRANSPOSE,
            CoreRules.SORT_REMOVE_CONSTANT_KEYS,
            CoreRules.SORT_UNION_TRANSPOSE,
            CoreRules.EXCHANGE_REMOVE_CONSTANT_KEYS,
            CoreRules.SORT_EXCHANGE_REMOVE_CONSTANT_KEYS);


    public static final List<RelOptRule> ABSTRACT_RELATIONAL_RULES = ImmutableList.of(
            CoreRules.FILTER_INTO_JOIN,
            CoreRules.JOIN_CONDITION_PUSH,
            AbstractConverter.ExpandConversionRule.INSTANCE,
            //CoreRules.JOIN_COMMUTE,
            CoreRules.PROJECT_TO_SEMI_JOIN,
            CoreRules.JOIN_TO_SEMI_JOIN,
            CoreRules.AGGREGATE_REMOVE,
            CoreRules.UNION_TO_DISTINCT,
            CoreRules.PROJECT_REMOVE,
            CoreRules.AGGREGATE_JOIN_TRANSPOSE,
            CoreRules.AGGREGATE_MERGE,
            CoreRules.AGGREGATE_PROJECT_MERGE,
            CoreRules.CALC_REMOVE,
            CoreRules.SORT_REMOVE);


    public static final List<RelOptRule> ABSTRACT_RULES = ImmutableList.of(
            CoreRules.AGGREGATE_ANY_PULL_UP_CONSTANTS,
            CoreRules.UNION_PULL_UP_CONSTANTS,
            PruneEmptyRules.UNION_INSTANCE,
            PruneEmptyRules.INTERSECT_INSTANCE,
            PruneEmptyRules.MINUS_INSTANCE,
            PruneEmptyRules.PROJECT_INSTANCE,
            PruneEmptyRules.FILTER_INSTANCE,
            PruneEmptyRules.SORT_INSTANCE,
            PruneEmptyRules.AGGREGATE_INSTANCE,
            PruneEmptyRules.JOIN_LEFT_INSTANCE,
            PruneEmptyRules.JOIN_RIGHT_INSTANCE,
            PruneEmptyRules.SORT_FETCH_ZERO_INSTANCE,
            CoreRules.UNION_MERGE,
            CoreRules.INTERSECT_MERGE,
            CoreRules.MINUS_MERGE,
            CoreRules.PROJECT_TO_LOGICAL_PROJECT_AND_WINDOW,
            CoreRules.FILTER_MERGE,
            DateRangeRules.FILTER_INSTANCE,
            CoreRules.INTERSECT_TO_DISTINCT);
}
