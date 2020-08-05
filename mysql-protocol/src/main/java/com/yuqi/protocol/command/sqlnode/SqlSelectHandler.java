package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import com.yuqi.engine.operator.Operator;
import com.yuqi.protocol.command.sepcial.SpecialSelectHolder;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import com.yuqi.sql.rel.SlothRel;
import io.netty.buffer.ByteBuf;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 17:03
 **/
public class SqlSelectHandler implements Handler<SqlSelect> {

    public static final SqlSelectHandler INSTANCE = new SqlSelectHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlSelect type) {

        String s = type.toString();
        Handler handler;
        if ((handler = SpecialSelectHolder.SPECIAL_HANDLER.get(s)) != null) {
            handler.handle(connectionContext, s);
            return;
        }

        SlothParser slothParser = ParserFactory.getParser(connectionContext.getQueryString(),
                connectionContext.getDb());
        final RelNode relNode = slothParser.getPlan(type);


        final Operator operator = ((SlothRel) relNode).implement();

        //now start to start execute;
        final List<List<Object>> result = executeOperator(operator);

        final List<List<String>> data = result.stream()
                .map(l -> l.stream().map(Object::toString).collect(Collectors.toList()))
                .collect(Collectors.toList());


        //如果select * 之类就不能用这个sqlnode 拿列名
        List<String> columnNames = type.getSelectList().getList().stream().map(Object::toString).collect(Collectors.toList());
        int[] columnTypes = new int[columnNames.size()];

        for (int i = 0; i < columnNames.size(); i++) {
            columnTypes[i] = 0xfd;
        }

        final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                .columnName(columnNames.toArray(new String[columnNames.size()]))
                .columnType(columnTypes)
                .data(data)
                .schema(StringUtils.EMPTY)
                .table(StringUtils.EMPTY)
                .build();

        final ByteBuf byteBuf = PackageUtils.buildResultSet(resultSetHolder);
        connectionContext.write(byteBuf);
    }


    List<List<Object>> executeOperator(Operator operator) {
        final List<List<Object>> result = Lists.newArrayList();

        operator.open();

        List<Object> value;

        List<Value> tmp;
        while ((tmp = operator.next()) != null) {
            result.add(tmp.stream().map(v -> v.getValueByType()).collect(Collectors.toList()));
        }

        return result;
    }

}
