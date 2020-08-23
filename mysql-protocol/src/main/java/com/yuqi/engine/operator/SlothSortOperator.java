package com.yuqi.engine.operator;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.rex.RexToSymbolShuttle;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/8/20 18:56
 **/
public class SlothSortOperator extends AbstractOperator {
    private final List<RelFieldCollation> sort;
    private RexNode offset;
    private RexNode limit;

    private Operator input;
    private List<List<Value>> sortHolder = Lists.newArrayList();
    private Iterator<List<Value>> sortIterator;

    private int offsetValue;
    private int limitValue;

    private boolean haveFetchData = false;
    private int hasFetchNumberOfData = 0;

    public SlothSortOperator(List<RelFieldCollation> sort, RexNode offset, RexNode limit,
                             Operator input, RelDataType relDataType) {
        super(relDataType);

        this.sort = sort;
        this.offset = offset;
        this.limit = limit;
        this.input = input;

        offsetValue = offset == null ?  -1 : offset.accept(RexToSymbolShuttle.INSTANCE).compute().intValue();
        limitValue = limit == null ? -1 : limit.accept(RexToSymbolShuttle.INSTANCE).compute().intValue();
    }

    @Override
    public void open() {
        input.open();

        //do your own work;
    }

    @Override
    public List<Value> next() {
        if (limitValue != -1 && hasFetchNumberOfData >= limitValue) {
            return EOF;
        }

        List<Value> data = EOF;
        if (CollectionUtils.isEmpty(sort)) {
            int o = 0;
            if (!haveFetchData) {
                if (offsetValue > 0) {
                    while (o < offsetValue && input.next() != EOF) {
                        o++;
                    }

                    if (o < offsetValue) {
                        return EOF;
                    }
                }
                haveFetchData = true;
            }
            data = input.next();

            if (limitValue != -1 && data != EOF) {
                hasFetchNumberOfData++;
            }
            return data;
        } else {
            if (!haveFetchData) {
                while ((data = input.next()) != EOF) {
                    sortHolder.add(data);
                }

                haveFetchData = true;
                sortData(sortHolder);
                sortIterator = sortHolder.iterator();

                int o = 0;
                if (offsetValue > 0) {
                    while (o < offsetValue && sortIterator.hasNext()) {
                        sortIterator.next();
                        o++;
                    }

                    //说明offset 超过了实际总数据值
                    if (o != offsetValue) {
                        return EOF;
                    }
                }
            }

            if (sortIterator.hasNext()) {
                data = sortIterator.next();
                hasFetchNumberOfData++;
            }

            return data;
        }
    }

    @Override
    public void close() {

    }


    //直接内存排序, 如果数据量过大，可以考虑归并排
    private void sortData(List<List<Value>> data) {
        data.sort((a, b) -> sort(0, a, b));
    }

    private int sort(int i, List<Value> a, List<Value> b) {
        if (i >= sort.size()) {
            return 0;
        }

        RelFieldCollation relFieldCollation = sort.get(i);
        int index = relFieldCollation.getFieldIndex();
        RelFieldCollation.Direction direction = relFieldCollation.getDirection();

        int r = a.get(index).compareTo(b.get(index));
        if (r != 0) {
            return direction == RelFieldCollation.Direction.ASCENDING ? r : -r;
        }


        return sort(i + 1, a, b);
    }
}
