package com.yuqi.sql.rule;

import com.yuqi.sql.rel.TestRel;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterImpl;
import org.apache.calcite.rel.metadata.RelMetadataQuery;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 27/7/20 23:54
 **/
public class SlothToTestConverter extends ConverterImpl implements TestRel {

    public SlothToTestConverter(RelOptCluster cluster, RelTraitSet traits, RelNode child) {
        super(cluster, ConventionTraitDef.INSTANCE, traits, child);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq).multiplyBy(0.1);
    }

    @Override public RelNode copy(RelTraitSet traitSet, List<RelNode> inputs) {
        return new SlothToTestConverter(getCluster(), traitSet, sole(inputs));
    }

}
