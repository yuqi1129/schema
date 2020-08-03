package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothJoin;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalJoin;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 3/8/20 19:37
 **/
public class SlothJoinConverterRule extends AbstactSlothConverter {

    public static final SlothJoinConverterRule INSTANCE = new SlothJoinConverterRule(
            LogicalJoin.class,
            Convention.NONE,
            SlothConvention.INSTANCE,
            "SlothJoinConvertRule"
    );

    public SlothJoinConverterRule(Class<? extends RelNode> clazz, RelTrait in,
                                  Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
    }

    @Override
    public RelNode convert(RelNode rel) {
        final LogicalJoin logicalJoin = (LogicalJoin) rel;

        return new SlothJoin(
                logicalJoin.getCluster(),
                logicalJoin.getTraitSet().replace(out),
                logicalJoin.getHints(),
                convert(logicalJoin.getLeft(), out),
                convert(logicalJoin.getRight(), out),
                logicalJoin.getCondition(),
                logicalJoin.getVariablesSet(),
                logicalJoin.getJoinType());
    }
}
