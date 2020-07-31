package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothLogicalValues;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.logical.LogicalValues;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/7/20 19:45
 **/
public class LogicalValueToLogicalValueRule extends ConverterRule {

    public static final LogicalValueToLogicalValueRule INSTANCE = new LogicalValueToLogicalValueRule(
            LogicalValues.class,
            Convention.NONE,
            SlothConvention.SLOTH_CONVENTION,
            "LogicalValueToLogicalValueRule"
    );


    public LogicalValueToLogicalValueRule(Class<? extends RelNode> clazz, RelTrait in, RelTrait out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
    }

    @Override
    public RelNode convert(RelNode rel) {
        LogicalValues logicalValues = (LogicalValues) rel;
        return new SlothLogicalValues(rel.getCluster(), rel.getRowType(),
                logicalValues.tuples, logicalValues.getTraitSet().replace(SlothConvention.SLOTH_CONVENTION));
    }
}
