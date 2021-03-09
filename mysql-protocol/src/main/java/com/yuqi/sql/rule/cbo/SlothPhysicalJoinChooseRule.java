package com.yuqi.sql.rule.cbo;

import com.yuqi.sql.PhysicalJoinType;
import com.yuqi.sql.rel.SlothHashJoin;
import com.yuqi.sql.rel.SlothJoin;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelOptRuleOperand;
import org.apache.calcite.rel.RelNode;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 2/3/21 下午8:52
 **/
public class SlothPhysicalJoinChooseRule extends RelOptRule {

    public static final SlothPhysicalJoinChooseRule INSTANCE = new SlothPhysicalJoinChooseRule(
        operand(SlothJoin.class, any()), "SlothPhysicalJoinChooseRule");

    public SlothPhysicalJoinChooseRule(RelOptRuleOperand operand, String description) {
        super(operand, description);
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        SlothJoin slothJoin = call.rel(0);

        RelNode left = slothJoin.getLeft();
        RelNode right = slothJoin.getRight();

//
//        RelOptCost leftCost = left instanceof RelSubset ? ((RelSubset) left).getWinnerCost() :
//            left.computeSelfCost(call.getPlanner(), call.getMetadataQuery());
//
//        RelOptCost rightCost = right instanceof RelSubset ? ((RelSubset) right).getWinnerCost() :
//            right.computeSelfCost(call.getPlanner(), call.getMetadataQuery());

        //假如说这里sort Merge
//        if (leftCost.getRows() >= 1000 || rightCost.getRows() >= 1000) {
            // First directly use hash join
            SlothHashJoin res = new SlothHashJoin(
                slothJoin.getCluster(),
                slothJoin.getTraitSet(),
                slothJoin.getHints(),
                slothJoin.getLeft(),
                slothJoin.getRight(),
                slothJoin.getCondition(),
                slothJoin.getVariablesSet(),
                slothJoin.getJoinType());

            res.setPhysicalNode(PhysicalJoinType.HASH_JOIN);
            call.transformTo(res);
//        }
    }
}
