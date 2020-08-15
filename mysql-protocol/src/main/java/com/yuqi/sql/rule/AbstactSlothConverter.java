package com.yuqi.sql.rule;


import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 3/8/20 19:28
 **/
public abstract class AbstactSlothConverter extends ConverterRule {
    protected final Convention out;

    public AbstactSlothConverter(Class<? extends RelNode> clazz, RelTrait in,
                                 Convention out, String descriptionPrefix) {
        super(clazz, in, out, descriptionPrefix);
        this.out = out;
    }
}
