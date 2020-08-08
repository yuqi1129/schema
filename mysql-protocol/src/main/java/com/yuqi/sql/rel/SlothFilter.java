package com.yuqi.sql.rel;

import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothFilterOperator;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Filter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;


/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 3/8/20 19:17
 **/
public class SlothFilter extends Filter implements SlothRel {

    public SlothFilter(RelOptCluster cluster, RelTraitSet traits, RelNode child, RexNode condition) {
        super(cluster, traits, child, condition);
    }

    @Override
    public Filter copy(RelTraitSet traitSet, RelNode input, RexNode condition) {
        return new SlothFilter(input.getCluster(), traitSet, input, condition);
    }

    @Override
    public Operator implement() {
        final Operator input = ((SlothRel) getInput()).implement();
        RelDataType relDataType = this.rowType;
        RexNode condition = this.condition;

        return new SlothFilterOperator(condition, input, relDataType);
    }
}
