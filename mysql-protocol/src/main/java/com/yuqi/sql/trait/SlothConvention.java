package com.yuqi.sql.trait;

import com.yuqi.sql.rel.SlothRel;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.plan.RelTraitSet;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 27/7/20 20:20
 **/
public class SlothConvention implements Convention {

    public static final SlothConvention SLOTH_CONVENTION = new SlothConvention();
    @Override
    public Class getInterface() {
        return SlothRel.class;
    }

    @Override
    public String getName() {
        return "Sloth";
    }

    @Override
    public boolean canConvertConvention(Convention toConvention) {
        return false;
    }

    @Override
    public boolean useAbstractConvertersForConversion(RelTraitSet fromTraits, RelTraitSet toTraits) {
        return false;
    }

    @Override
    public RelTraitDef getTraitDef() {
        return ConventionTraitDef.INSTANCE;
    }

    @Override
    public boolean satisfies(RelTrait trait) {
        return this == trait;
    }

    @Override
    public void register(RelOptPlanner planner) {

    }

    @Override
    public String toString() {
        return "SlothConvention";
    }
}
