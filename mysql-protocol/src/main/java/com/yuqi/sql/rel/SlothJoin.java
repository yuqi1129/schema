package com.yuqi.sql.rel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.yuqi.engine.SlothRow;
import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothJoinOperator;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.CorrelationId;
import org.apache.calcite.rel.core.Join;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.hint.RelHint;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import java.util.List;
import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 3/8/20 19:19
 **/
public class SlothJoin extends Join implements SlothRel {

    //TODO konw something about join hint
    public SlothJoin(RelOptCluster cluster, RelTraitSet traitSet, List<RelHint> hints, RelNode left, RelNode right,
                     RexNode condition, Set<CorrelationId> variablesSet, JoinRelType joinType) {
        super(cluster, traitSet, hints, left, right, condition, variablesSet, joinType);
    }

    @Override
    public Join copy(RelTraitSet traitSet, RexNode conditionExpr, RelNode left, RelNode right,
                     JoinRelType joinType, boolean semiJoinDone) {

        //pay attention to variablesSet and semiJoinDone
        return new SlothJoin(left.getCluster(), traitSet, ImmutableList.of(), left, right,
                condition, ImmutableSet.of(), joinType);
    }

    @Override
    public Operator implement() {
        final Operator<SlothRow> left = ((SlothRel) getLeft()).implement();
        final Operator<SlothRow> right = ((SlothRel) getRight()).implement();
        final RexNode jontCondition = getCondition();
        final RelDataType rowType = getRowType();

        return new SlothJoinOperator(left, right, jontCondition, joinType, rowType);
    }
}
