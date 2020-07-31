package com.yuqi.sql.rule;

import com.yuqi.sql.trait.SlothConvention;
import com.yuqi.sql.trait.TestConvention;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.core.RelFactories;

import java.util.function.Predicate;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/7/20 23:14
 **/
public class SlothToTestConverterRule extends ConverterRule {

    public static final ConverterRule INSTANCE = new SlothToTestConverterRule();

    public SlothToTestConverterRule() {
        super(RelNode.class, (Predicate<RelNode>) r -> true,
                SlothConvention.SLOTH_CONVENTION, TestConvention.INSTANCE,
                RelFactories.LOGICAL_BUILDER, "ElasticsearchToEnumerableConverterRule");    }

    @Override
    public RelNode convert(RelNode relNode) {
        RelTraitSet newTraitSet = relNode.getTraitSet().replace(getOutConvention());
        return new SlothToTestConverter(relNode.getCluster(), newTraitSet, relNode);
    }
}
