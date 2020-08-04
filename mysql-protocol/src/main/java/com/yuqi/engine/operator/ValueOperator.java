package com.yuqi.engine.operator;

import com.google.common.collect.ImmutableList;
import com.yuqi.engine.io.IO;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rex.RexLiteral;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/8/20 18:52
 **/
public class ValueOperator implements Operator, IO {
    private final ImmutableList<ImmutableList<RexLiteral>> values;
    private final RelDataType relDataType;


    private Iterator<List<Object>> r;
    private List<String> types;

    public ValueOperator(ImmutableList<ImmutableList<RexLiteral>> values, RelDataType relDataType) {
        this.values = values;
        this.relDataType = relDataType;
    }

    @Override
    public void open() {
        List<RelDataType> columnsTypes = relDataType.getFieldList().stream()
                .map(RelDataTypeField::getType)
                .collect(Collectors.toList());


        r = values.stream().map(l -> l.stream()
                .map(RexLiteral::getValue2).collect(Collectors.toList()))
                .collect(Collectors.toList())
                .iterator();

    }

    @Override
    public List<Object> next() {

        if (r.hasNext()) {
            return r.next();
        }

        return null;
    }

    @Override
    public void close() {

    }
}
