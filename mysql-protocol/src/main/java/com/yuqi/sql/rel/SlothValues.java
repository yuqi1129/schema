package com.yuqi.sql.rel;

import com.google.common.collect.ImmutableList;
import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothValueOperator;
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
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 27/7/20 21:15
 **/
public class SlothValues extends Values implements SlothRel {

    public SlothValues(RelOptCluster cluster, RelDataType rowType, ImmutableList<ImmutableList<RexLiteral>> tuples, RelTraitSet traits) {
        super(cluster, rowType, tuples, traits);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }

    @Override
    public void register(RelOptPlanner planner) {
        super.register(planner);
    }


    @Override
    public Operator implement() {
        return new SlothValueOperator(tuples, rowType);
    }
}
