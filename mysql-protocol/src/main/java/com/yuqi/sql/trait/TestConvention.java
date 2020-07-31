package com.yuqi.sql.trait;

import com.yuqi.sql.rel.TestRel;
import org.apache.calcite.plan.Convention;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelTrait;
import org.apache.calcite.plan.RelTraitDef;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/7/20 23:01
 **/
public class TestConvention implements Convention {

    public static final TestConvention INSTANCE = new TestConvention();
    @Override
    public Class getInterface() {
        return TestRel.class;
    }

        @Override
    public String getName() {
        return "Test";
    }

    @Override
    public RelTraitDef getTraitDef() {
        return ConventionTraitDef.INSTANCE;
    }

    //应该需要长记性，这里需要实现，默认是false, 导致不会执行代价计算，从而导致最终有问题
    @Override
    public boolean satisfies(RelTrait relTrait) {
        return this == relTrait;
    }

    @Override
    public void register(RelOptPlanner relOptPlanner) {

    }

    @Override
    public String toString() {
        return "TestConvention";
    }
}
