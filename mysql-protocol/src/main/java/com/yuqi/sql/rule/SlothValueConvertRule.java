package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothValues;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.logical.LogicalValues;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 27/7/20 21:27
 **/
public class SlothValueConvertRule extends ConverterRule {

    private final Convention out;

    public static final SlothValueConvertRule INSTANCE =
            new SlothValueConvertRule(
                    LogicalValues.class,
                    Convention.NONE,
                    SlothConvention.INSTANCE,
                    "SlothValueConverter"
            );
    public SlothValueConvertRule(Class<? extends RelNode> clazz, RelTrait in, Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
        this.out = out;
    }

    @Override
    public RelNode convert(RelNode rel) {
        LogicalValues logicalValues = (LogicalValues) rel;
        return new SlothValues(
                logicalValues.getCluster(),
                logicalValues.getRowType(),
                logicalValues.getTuples(),
                RelTraitSet.createEmpty().plus(SlothConvention.INSTANCE));
    }
}
