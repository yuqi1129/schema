package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothSort;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalSort;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/8/20 17:23
 **/
public class SlothSortConverterRule extends AbstactSlothConverter {

    public static final SlothSortConverterRule INSTANCE = new SlothSortConverterRule(
            LogicalSort.class,
            Convention.NONE,
            SlothConvention.INSTANCE,
            "SlothSortConverterRule"
    );

    public SlothSortConverterRule(Class<? extends RelNode> clazz, RelTrait in, Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
    }

    @Override
    public RelNode convert(RelNode rel) {
        LogicalSort logicalSort = (LogicalSort) rel;

        return new SlothSort(
                logicalSort.getCluster(),
                logicalSort.getTraitSet().replace(out),
                convert(logicalSort.getInput(), out),
                logicalSort.getCollation(),
                logicalSort.offset,
                logicalSort.fetch);
    }
}
