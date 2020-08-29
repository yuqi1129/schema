package com.yuqi.sql.rel;

import com.yuqi.engine.SlothRow;
import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothSortOperator;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/8/20 20:14
 **/
public class SlothSort extends Sort implements SlothRel<SlothRow> {

    public SlothSort(RelOptCluster cluster, RelTraitSet traits, RelNode child, RelCollation collation) {
        super(cluster, traits, child, collation);
    }

    public SlothSort(RelOptCluster cluster, RelTraitSet traits, RelNode child, RelCollation collation, RexNode offset, RexNode fetch) {
        super(cluster, traits, child, collation, offset, fetch);
    }

    @Override
    public Sort copy(RelTraitSet traitSet, RelNode newInput, RelCollation newCollation, RexNode offset, RexNode fetch) {
        return new SlothSort(input.getCluster(), traitSet, newInput, collation, offset, fetch);
    }

    @Override
    public Operator<SlothRow> implement() {

        //Sort has bug
        //result of sql 'select id, name from person order by id + 1' has three column
        Operator<SlothRow> input = ((SlothRel) getInput()).implement();
        final List<RelFieldCollation> sortKeys = this.collation.getFieldCollations();
        final RelDataType relDataType = getRowType();
        return new SlothSortOperator(sortKeys, offset, fetch, input, relDataType);
    }
}
