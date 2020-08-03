package com.yuqi.sql.rule;

import com.yuqi.sql.rel.SlothProject;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;
import org.apache.calcite.rel.logical.LogicalProject;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 27/7/20 21:17
 **/
public class SlothProjectConvertRule extends ConverterRule {

    private final Convention out;

    public static final SlothProjectConvertRule INSTANCE =
            new SlothProjectConvertRule(
                    LogicalProject.class,
                    Convention.NONE,
                    SlothConvention.INSTANCE,
                    "SlothProjectConverter");


    public SlothProjectConvertRule(Class<? extends RelNode> clazz,
                                   RelTrait in, Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
        this.out = out;
    }

    @Override
    public RelNode convert(RelNode rel) {
        LogicalProject logicalProject = (LogicalProject) rel;
        return new SlothProject(
                logicalProject.getCluster(),
                logicalProject.getTraitSet().replace(out),
                convert(logicalProject.getInput(), out),
                logicalProject.getProjects(),
                logicalProject.getRowType());
    }
}
