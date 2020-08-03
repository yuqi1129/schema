package com.yuqi.sql.rel;

import com.google.common.collect.ImmutableList;
import com.yuqi.sql.rule.SlothToTestConverterRule;
import com.yuqi.sql.rule.SlothProjectConvertRule;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.core.Values;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexLiteral;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/7/20 19:46
 **/
public class SlothLogicalValues extends Values implements SlothRel {
    public SlothLogicalValues(RelOptCluster cluster, RelDataType rowType,
                              ImmutableList<ImmutableList<RexLiteral>> tuples, RelTraitSet traits) {
        super(cluster, rowType, tuples, traits);
    }

    @Override
    public void register(RelOptPlanner planner) {
        //super.register(planner);
        planner.addRule(SlothToTestConverterRule.INSTANCE);
        planner.addRule(SlothProjectConvertRule.INSTANCE);
        //planner.addRule(SlothValueConvertRule.INSTANCE);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }
}
