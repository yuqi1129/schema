package com.yuqi.sql.rel;

import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rex.RexNode;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/8/20 20:14
 **/
public class SlothSort extends Sort implements SlothRel {

    public SlothSort(RelOptCluster cluster, RelTraitSet traits, RelNode child, RelCollation collation) {
        super(cluster, traits, child, collation);
    }

    @Override
    public Sort copy(RelTraitSet traitSet, RelNode newInput, RelCollation newCollation, RexNode offset, RexNode fetch) {
        return new SlothSort(input.getCluster(), traitSet, newInput, collation);
    }
}
