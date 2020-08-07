package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothAggregate;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalAggregate;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 7/8/20 14:25
 **/
public class SlothAggregateConverterRule extends AbstactSlothConverter {

    public static final SlothAggregateConverterRule INSTANCE = new SlothAggregateConverterRule(
            LogicalAggregate.class,
            Convention.NONE,
            SlothConvention.INSTANCE,
            "SlothAggregateConverterRule"

    );

    public SlothAggregateConverterRule(Class<? extends RelNode> clazz, RelTrait in, Convention out,
                                       String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
    }

    @Override
    public RelNode convert(RelNode rel) {
        final LogicalAggregate logicalAggregate = (LogicalAggregate) rel;
        return new SlothAggregate(
                logicalAggregate.getCluster(),
                logicalAggregate.getTraitSet().replace(out),
                logicalAggregate.getHints(),
                convert(logicalAggregate.getInput(), out),
                logicalAggregate.getGroupSet(),
                logicalAggregate.getGroupSets(),
                logicalAggregate.getAggCallList());
    }
}
