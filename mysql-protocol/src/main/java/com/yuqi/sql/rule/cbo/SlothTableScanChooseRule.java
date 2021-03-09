package com.yuqi.sql.rule.cbo;

import com.yuqi.sql.rel.SlothTableScan;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelOptRuleOperand;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 8/3/21 下午8:54
 **/
public class SlothTableScanChooseRule extends RelOptRule {
    public static final SlothTableScanChooseRule INSTANCE = new SlothTableScanChooseRule(
        operand(SlothTableScan.class, any()), "SlothPhysicalJoinChooseRule");


    public SlothTableScanChooseRule(RelOptRuleOperand operand, String description) {
        super(operand, description);
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        // 选择相应的TableScan
        // 全扫、索引扫、索引覆盖
        //如果是 索引覆盖 则可以在RelTraitSet中加入CollationSet, 然后比如说Filter/Scan Rule 之后， Filter 也加上了CollationSet
        //如果 Join 的话，如果两边都是CollationJoin, 那么大概率选ColllationJoin
        //Sort Rule 需要给Output 加上CollationTraitSet
        //TODO



    }
}
