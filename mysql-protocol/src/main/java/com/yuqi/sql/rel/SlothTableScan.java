package com.yuqi.sql.rel;

import com.yuqi.engine.SlothRow;
import com.yuqi.engine.operator.Operator;
import com.yuqi.engine.operator.SlothTableScanOperator;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptCost;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.core.TableScan;
import org.apache.calcite.rel.metadata.RelMetadataQuery;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;

import java.util.List;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 28/7/20 20:22
 **/
public class SlothTableScan extends TableScan implements SlothRel<SlothRow> {

    /**
     * Project push to tables scan
     */
    private List<RexNode> projects;

    /**
     * if projects is not null, this should be set
     */
    private RelDataType outputType;

    /**
     * condition push down to table scan
     * Attention some can use index and some not
     * only those can use index will take effect actually
     */
    private RexNode condition;


    public SlothTableScan(RelOptCluster cluster, RelTraitSet traitSet, RelOptTable table) {
        super(cluster, traitSet, table);
    }

    public List<RexNode> getProjects() {
        return projects;
    }

    public void setProjects(List<RexNode> projects) {
        this.projects = projects;
    }

    public RexNode getCondition() {
        return condition;
    }

    public void setCondition(RexNode condition) {
        this.condition = condition;
    }

    public void setOutputType(RelDataType outputType) {
        this.outputType = outputType;
    }

    @Override
    public RelOptCost computeSelfCost(RelOptPlanner planner, RelMetadataQuery mq) {
        //TODO
        double dRows = table.getQualifiedName().contains("v1") ? 1D : table.getRowCount();
        double dCpu = dRows + 1; // ensure non-zero cost
        double dIo = 0;
        return planner.getCostFactory().makeCost(dRows, dCpu, dIo);
    }

    @Override
    public Operator<SlothRow> implement() {
        //TODO 这里需要统一type 转化
        return new SlothTableScanOperator(this.table, this.rowType, outputType, projects, condition);
    }

    @Override
    public RelDataType deriveRowType() {
        if (Objects.nonNull(outputType)) {
            return outputType;
        }

        return super.deriveRowType();
    }

    @Override
    public double estimateRowCount(RelMetadataQuery mq) {
        if (this.table.getQualifiedName().contains("v1")) {
            return 1;
        }

        if (this.table.getQualifiedName().contains("view1_tpch_lineitem")) {
            return 1;
        }

        if (this.table.getQualifiedName().contains("view2")) {
            return 1;
        }

        return this.getTable().getRowCount();
    }
}
