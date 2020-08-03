package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothFilter;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalFilter;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 3/8/20 19:26
 **/
public class SlothFilterConverterRule extends AbstactSlothConverter {

    public static final SlothFilterConverterRule INSTANCE = new SlothFilterConverterRule(
            LogicalFilter.class,
            Convention.NONE,
            SlothConvention.INSTANCE,
            "SlothFilterConvertRule"

    );

    public SlothFilterConverterRule(Class<? extends RelNode> clazz, RelTrait in,
                                    Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
    }

    @Override
    public RelNode convert(RelNode rel) {
        final LogicalFilter logicalFilter = (LogicalFilter) rel;
        return new SlothFilter(
                logicalFilter.getCluster(),
                logicalFilter.getTraitSet().replace(out),
                //use convert instead
                convert(logicalFilter.getInput(), out),
                logicalFilter.getCondition());
    }
}
