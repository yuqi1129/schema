package com.yuqi.sql.rel;

import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothAggregateOperator;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Aggregate;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.rel.hint.RelHint;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.util.ImmutableBitSet;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 7/8/20 14:20
 **/
public class SlothAggregate extends Aggregate implements SlothRel {
    public SlothAggregate(RelOptCluster cluster, RelTraitSet traitSet, List<RelHint> hints, RelNode input,
                          ImmutableBitSet groupSet, List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
        super(cluster, traitSet, hints, input, groupSet, groupSets, aggCalls);
    }

    @Override
    public Aggregate copy(RelTraitSet traitSet, RelNode input, ImmutableBitSet groupSet,
                          List<ImmutableBitSet> groupSets, List<AggregateCall> aggCalls) {
        return new SlothAggregate(getCluster(), traitSet, getHints(), input, groupSet, groupSets, aggCalls);
    }

    @Override
    public Operator implement() {
        final Operator input = ((SlothRel) getInput()).implement();

        //select id from person group by id
        //select distict id from person;
        //TODO
        final RelDataType rowType = getRowType();
        return new SlothAggregateOperator(input, groupSet, groupSets, aggCalls, rowType);
    }
}
