package com.yuqi.sql.rule;

import com.google.common.collect.ImmutableList;
import org.apache.calcite.plan.RelOptRule;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 12/7/20 20:57
 **/
public class SlothRules {

    public static final List<RelOptRule> CONVERTER_RULE = ImmutableList.of(
            SlothProjectConvertRule.INSTANCE,
            SlothValueConvertRule.INSTANCE

    );
}
