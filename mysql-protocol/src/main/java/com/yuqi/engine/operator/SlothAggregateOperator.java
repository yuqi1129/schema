package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.engine.data.func.agg.AbstractAggregation;
import com.yuqi.engine.data.func.agg.CountAggregation;
import com.yuqi.engine.data.func.agg.MaxAggregation;
import com.yuqi.engine.data.func.agg.MinAggregation;
import com.yuqi.engine.data.func.agg.SumAggregation;
import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.io.IO;
import com.yuqi.sql.util.TypeConversionUtils;
import org.apache.calcite.rel.core.AggregateCall;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.util.ImmutableBitSet;
import org.apache.commons.collections4.CollectionUtils;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.yuqi.engine.operator.SlothTableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:38
 **/
public class SlothAggregateOperator implements Operator, IO {

    private Operator input;
    private ImmutableBitSet groupset;

    //currently do know the use;
    private List<ImmutableBitSet> groupSets;
    private List<AggregateCall> aggregateCalls;

    private List<Integer> groupByIndex;
    private List<Symbol> aggCalls;

    private List<List<Value>> valueHolder = Lists.newArrayList();
    private boolean hasFetchData = false;
    private Iterator<List<Value>> valueIterator;

    public SlothAggregateOperator(Operator input, ImmutableBitSet groupset, List<ImmutableBitSet> groupSets, List<AggregateCall> aggregateCalls) {
        this.input = input;
        this.groupset = groupset;
        this.groupSets = groupSets;
        this.aggregateCalls = aggregateCalls;
    }

    @Override
    public void open() {
        input.open();

        groupByIndex = groupset.asList();
        //do your owner work
    }

    @Override
    public List<Value> next() {
        List<Value> v = EOF;
        if (!hasFetchData) {
            while ((v = input.next()) != EOF) {
                valueHolder.add(v);
            }

            if (CollectionUtils.isNotEmpty(groupByIndex)) {
                valueIterator = getResultWithGroupBy(valueHolder);
            } else {
                valueIterator = getResultWithOutGroupBy(valueHolder);
            }

            hasFetchData = true;
        }

        if (valueIterator.hasNext()) {
            v = valueIterator.next();
        }

        return v;

    }

    @Override
    public void close() {
        input.close();

        //do your work
    }

    private Iterator<List<Value>> getResultWithGroupBy(List<List<Value>> valueHolder) {
        List<AbstractAggregation> aggregations = createAggregation();
        return valueHolder.stream().collect(Collectors.groupingBy(valueList -> {
            final StringBuilder builder = new StringBuilder();

            for (Integer i : groupByIndex) {
                //TODO 用MD5拼key
                builder.append(valueList.get(i).stringValue()).append("<------------>");
            }
            return builder.toString();
        })).values().stream().map(l -> {
            List<Value> g = aggregations.stream().map(agg -> {
                agg.setOriginDatas(l);
                return agg.compute();
            }).collect(Collectors.toList());

            List<Value> r = Lists.newArrayList();
            for (Integer i : groupByIndex) {
                //取第一个数据 做为rowkey
                r.add(l.get(0).get(i).copy());
            }

            r.addAll(g);
            return r;
        }).iterator();
    }

    private Iterator<List<Value>> getResultWithOutGroupBy(List<List<Value>> valueHolder) {
        final List<AbstractAggregation> abstractAggregations = createAggregation();

        final List<Value> values = abstractAggregations.stream().map(a -> {
            a.setOriginDatas(valueHolder);
            return a.compute();

        }).collect(Collectors.toList());
        final List<List<Value>> r = Lists.newArrayList();
        r.add(values);

        return r.iterator();
    }

    private List<AbstractAggregation> createAggregation() {
       return aggregateCalls.stream().map(call -> {
            final SqlAggFunction function = call.getAggregation();
            final List<Integer> args = call.getArgList();
            final int index = args.size() == 0 ? -1 : args.get(0);

            final AbstractAggregation r;
            if (function == SqlStdOperatorTable.SUM) {
                r = new SumAggregation(
                        call.isDistinct(),
                        call.ignoreNulls(),
                        null,
                        TypeConversionUtils.getBySqlTypeName(call.type.getSqlTypeName()),
                        index,
                        groupByIndex);
            } else if (function == SqlStdOperatorTable.COUNT) {
                r = new CountAggregation(
                        call.isDistinct(),
                        call.ignoreNulls(),
                        null,
                        TypeConversionUtils.getBySqlTypeName(call.type.getSqlTypeName()),
                        index,
                        args.isEmpty(),
                        groupByIndex);
            } else if (function == SqlStdOperatorTable.MAX) {
                r = new MaxAggregation(
                        call.isDistinct(),
                        call.ignoreNulls(),
                        null,
                        TypeConversionUtils.getBySqlTypeName(call.type.getSqlTypeName()),
                        index,
                        groupByIndex
                );
            } else {
                r = new MinAggregation(
                        call.isDistinct(),
                        call.ignoreNulls(),
                        null,
                        TypeConversionUtils.getBySqlTypeName(call.type.getSqlTypeName()),
                        index,
                        groupByIndex
                );
            }

            return r;
        }).collect(Collectors.toList());
    }
}
