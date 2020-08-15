package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothTableScan;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalTableScan;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/8/20 11:16
 **/
public class SlothTableScanConverterRule extends AbstactSlothConverter {

    public static final SlothTableScanConverterRule INSTANCE = new SlothTableScanConverterRule(
            LogicalTableScan.class,
            Convention.NONE,
            SlothConvention.INSTANCE,
            "SlothTableScanConverterRule"
    );

    public SlothTableScanConverterRule(Class<? extends RelNode> clazz, RelTrait in,
                                       Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
    }

    @Override
    public RelNode convert(RelNode rel) {
        final LogicalTableScan logicalTableScan = (LogicalTableScan) rel;

        return new SlothTableScan(
                logicalTableScan.getCluster(),
                logicalTableScan.getTraitSet().replace(out),
                rel.getTable());
    }
}
