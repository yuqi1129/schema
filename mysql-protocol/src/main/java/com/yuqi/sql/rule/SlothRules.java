package com.yuqi.sql.rule;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.config.CalciteSystemProperty;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.rel.rules.AggregateExpandDistinctAggregatesRule;
import org.apache.calcite.rel.rules.AggregateReduceFunctionsRule;
import org.apache.calcite.rel.rules.AggregateStarTableRule;
import org.apache.calcite.rel.rules.FilterAggregateTransposeRule;
import org.apache.calcite.rel.rules.FilterJoinRule;
import org.apache.calcite.rel.rules.FilterProjectTransposeRule;
import org.apache.calcite.rel.rules.FilterTableScanRule;
import org.apache.calcite.rel.rules.JoinAssociateRule;
import org.apache.calcite.rel.rules.JoinCommuteRule;
import org.apache.calcite.rel.rules.JoinPushExpressionsRule;
import org.apache.calcite.rel.rules.JoinPushThroughJoinRule;
import org.apache.calcite.rel.rules.ProjectFilterTransposeRule;
import org.apache.calcite.rel.rules.ProjectMergeRule;
import org.apache.calcite.rel.rules.ProjectWindowTransposeRule;
import org.apache.calcite.rel.rules.SortJoinTransposeRule;
import org.apache.calcite.rel.rules.SortProjectTransposeRule;
import org.apache.calcite.rel.rules.SortRemoveConstantKeysRule;
import org.apache.calcite.rel.rules.SortUnionTransposeRule;
import org.apache.calcite.rel.rules.TableScanRule;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 12/7/20 20:57
 **/
public class SlothRules {

    /**
     * Sloth default rule
     */
    public static final List<RelOptRule> DEFAULT_RULES =
            ImmutableList.of(
                    AggregateStarTableRule.INSTANCE,
                    AggregateStarTableRule.INSTANCE2,
                    TableScanRule.INSTANCE,
                    CalciteSystemProperty.COMMUTE.value()
                            ? JoinAssociateRule.INSTANCE
                            : ProjectMergeRule.INSTANCE,
                    FilterTableScanRule.INSTANCE,
                    ProjectFilterTransposeRule.INSTANCE,
                    FilterProjectTransposeRule.INSTANCE,
                    FilterJoinRule.FILTER_ON_JOIN,
                    JoinPushExpressionsRule.INSTANCE,
                    AggregateExpandDistinctAggregatesRule.INSTANCE,
                    AggregateReduceFunctionsRule.INSTANCE,
                    FilterAggregateTransposeRule.INSTANCE,
                    ProjectWindowTransposeRule.INSTANCE,
                    JoinCommuteRule.INSTANCE,
                    JoinPushThroughJoinRule.RIGHT,
                    JoinPushThroughJoinRule.LEFT,
                    SortProjectTransposeRule.INSTANCE,
                    SortJoinTransposeRule.INSTANCE,
                    SortRemoveConstantKeysRule.INSTANCE,
                    SortUnionTransposeRule.INSTANCE);
}
