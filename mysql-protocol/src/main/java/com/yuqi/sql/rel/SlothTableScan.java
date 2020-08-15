package com.yuqi.sql.rel;

import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothTableScanOperator;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.metadata.RelMetadataQuery;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 28/7/20 20:22
 **/
public class SlothTableScan extends TableScan implements SlothRel {
    public SlothTableScan(RelOptCluster cluster, RelTraitSet traitSet, RelOptTable table) {
        super(cluster, traitSet, table);
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        return super.computeSelfCost(planner, mq);
    }

    @Override
    public Operator implement() {
        //TODO 这里需要统一type 转化
        return new SlothTableScanOperator(this.table, this.rowType);
    }
}
