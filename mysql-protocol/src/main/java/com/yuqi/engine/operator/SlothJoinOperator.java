package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.expr.Symbol;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.rex.RexToSymbolShuttle;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexInputRef;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexShuttle;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;

import java.util.List;
import java.util.stream.Collectors;

import static com.yuqi.engine.operator.SlothTableScanOperator.EOF;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:22
 **/
public class SlothJoinOperator extends AbstractOperator {

    private Operator left;
    private Operator right;
    private RexNode joinCondition;
    private JoinRelType joinType;


    private Symbol joinConditionSymbol;

    //是否是第一次取数
    private boolean hasFetchData = false;

    //待排序的中间数据, 目前数据都直接存入内存
    private List<List<Value>> leftValueHodler;
    private List<List<Value>> rigthValueHodler;

    //读index
    private int leftReadIndex = 0;
    private int rightReadIndex = 0;

    private List<Integer> leftJoinIndex;
    private List<Integer> rightJoinIndex;

    //标识在left/right join 中是否join到数据了
    private boolean flag = false;

    private List<DataType> leftTypes;
    private List<DataType> rightTypes;

    public SlothJoinOperator(Operator left, Operator right, RexNode joinCondition,
                             JoinRelType joinType, RelDataType rowType) {
        super(rowType);
        this.left = left;
        this.right = right;
        this.joinCondition = joinCondition;
        this.joinType = joinType;
    }

    @Override
    public void open() {
        left.open();
        right.open();

        leftTypes = left.getRowType();
        rightTypes = right.getRowType();

        joinConditionSymbol = joinCondition.accept(RexToSymbolShuttle.INSTANCE);
        final RexInputShuttle rexInputShuttle = new RexInputShuttle(leftTypes.size());
        joinCondition.accept(rexInputShuttle);

        this.leftJoinIndex = rexInputShuttle.leftIndex;
        this.rightJoinIndex = rexInputShuttle.rightIndex;

    }

    @Override
    public List<Value> next() {
        if (!hasFetchData) {
            init();
        }

        switch (joinType) {
            case INNER:
                return handleInnerJoin();
            case LEFT:
                return handleLeftJoin();
            case RIGHT:
                return handleRightJoin();
            default:
                return handleFullJoin();
        }
    }

    private List<Value> handleInnerJoin() {
        //直nestloop, 也可以sort merge/hash
        List<Value> leftValue = EOF;
        while (leftReadIndex < leftValueHodler.size()) {
            leftValue = leftValueHodler.get(leftReadIndex);
            while (rightReadIndex < rigthValueHodler.size()) {
                List<Value> mergeValue = Lists.newArrayList(leftValue);
                mergeValue.addAll(rigthValueHodler.get(rightReadIndex));
                joinConditionSymbol.setInput(mergeValue);
                rightReadIndex++;
                if (joinConditionSymbol.compute().booleanValue()) {
                    return mergeValue.stream().map(Value::copy).collect(Collectors.toList());
                }
            }

            leftReadIndex++;
            rightReadIndex = 0;
        }
        return EOF;
    }


    private List<Value> handleLeftJoin() {
        List<Value> leftValue = EOF;
        while (leftReadIndex < leftValueHodler.size()) {
            leftValue = leftValueHodler.get(leftReadIndex);
            while (rightReadIndex < rigthValueHodler.size()) {
                List<Value> mergeValue = Lists.newArrayList(leftValue);
                mergeValue.addAll(rigthValueHodler.get(rightReadIndex));

                joinConditionSymbol.setInput(mergeValue);
                rightReadIndex++;
                if (joinConditionSymbol.compute().booleanValue()) {
                    flag = true;
                    return mergeValue.stream().map(Value::copy).collect(Collectors.toList());
                }
            }

            leftReadIndex++;
            rightReadIndex = 0;

            if (!flag) {
                for (DataType dataType : rightTypes) {
                    leftValue.add(new Value(null, dataType));
                }
                return leftValue.stream().map(Value::copy).collect(Collectors.toList());
            }

            flag = false;

        }
        return EOF;
    }

    private List<Value> handleRightJoin() {
        List<Value> rigthValue = EOF;
        while (rightReadIndex < rigthValueHodler.size()) {
            rigthValue = rigthValueHodler.get(rightReadIndex);
            while (leftReadIndex < leftValueHodler.size()) {
                List<Value> mergeValue = Lists.newArrayList(leftValueHodler.get(leftReadIndex));
                mergeValue.addAll(rigthValue);

                joinConditionSymbol.setInput(mergeValue);
                leftReadIndex++;
                if (joinConditionSymbol.compute().booleanValue()) {
                    flag = true;
                    return mergeValue.stream().map(Value::copy).collect(Collectors.toList());
                }
            }

            rightReadIndex++;
            leftReadIndex = 0;

            if (!flag) {
                List<Value> mergeValues = leftTypes.stream().map(t -> new Value(null, t)).collect(Collectors.toList());
                mergeValues.addAll(rigthValue);
                flag = false;
                return mergeValues.stream().map(Value::copy).collect(Collectors.toList());
            }

            flag = false;

        }
        return EOF;
    }


    private List<Value> handleFullJoin() {
        //MySQL do not support full out join
        return EOF;
    }


    private void init() {
        List<Value> leftValue;
        List<Value> rightValue;

        leftValueHodler = Lists.newArrayList();
        rigthValueHodler = Lists.newArrayList();

        while ((leftValue = left.next()) != EOF) {
            leftValueHodler.add(leftValue);
        }

        while ((rightValue = right.next()) != EOF) {
            rigthValueHodler.add(rightValue);
        }
        //sort;
        //sortData(leftValueHodler, leftJoinIndex);
        //sortData(rigthValueHodler, rightJoinIndex);

        hasFetchData = true;
    }

    @Override
    public void close() {
        left.close();
        right.close();
    }


    static class RexInputShuttle extends RexShuttle {

        private List<Integer> leftIndex = Lists.newArrayList();
        private List<Integer> rightIndex = Lists.newArrayList();

        private int leftRowTypeSize;

        RexInputShuttle(int leftRowTypeSize) {
            this.leftRowTypeSize = leftRowTypeSize;
        }

        @Override
        public RexNode visitCall(RexCall call) {
            //return super.visitCall(call);
            //只支持等值join
            if (SqlStdOperatorTable.EQUALS != call.getOperator()) {
                throw new RuntimeException("Currently we only support equal join...");
            }

            List<RexNode> operands = call.getOperands();
            leftIndex.add(((RexInputRef) operands.get(0)).getIndex());
            rightIndex.add(((RexInputRef) operands.get(1)).getIndex() - leftRowTypeSize);

            return super.visitCall(call);
        }
    }

    private void sortData(List<List<Value>> data, List<Integer> joinIndex) {
        data.sort((a, b) -> sort(0, joinIndex, a, b));
    }


    private int sort(int i, List<Integer> joinIndex, List<Value> a, List<Value> b) {
        if (i >= joinIndex.size()) {
            return 0;
        }
        int index = joinIndex.get(i);

        int r = a.get(index).compareTo(b.get(index));
        if (r != 0) {
            return r;
        }


        return sort(i + 1, joinIndex, a, b);
    }
}
