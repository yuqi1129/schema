package com.yuqi.protocol.command;

import com.google.common.collect.Lists;
import com.yuqi.protocol.command.sqlnode.Handler;
import com.yuqi.protocol.command.sqlnode.HandlerHolder;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import io.netty.buffer.ByteBuf;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 21:28
 **/
public class QueryCommandHandler extends AbstractCommandHandler {
    private String query;

    public QueryCommandHandler(ConnectionContext connectionContext, String query) {
        super(connectionContext);
        this.query = query;
    }

    @Override
    public void execute() {
        SqlNode sqlNode = null;
        //RelNode relNode = null;
        try {
            final SlothParser slothParser = ParserFactory.getParser(query);
            connectionContext.setQueryString(query);

            sqlNode = slothParser.getSqlNode();
            handleSqlNode(sqlNode);
        } catch (Exception e) {
            //TODO
            System.out.println(e);
            handleSqlString(query);
        }
    }


    private void handleSqlString(String command) {
        //todo
        defaultStringHandler(command);
    }

    private void handleSqlNode(SqlNode sqlNode) {
        final Class<?> sqlType = sqlNode.getClass();
        final Handler handler = HandlerHolder.SQL_TYPE_TO_HANDLER_MAP.get(sqlType);
        if (Objects.isNull(handler)) {
            //
            final MysqlPackage result = PackageUtils.buildErrPackage(1, "Do not support: " + sqlType, 1);
            connectionContext.getChannelHandlerContext().writeAndFlush(PackageUtils.packageToBuf(result));
            return;
        }
        handler.handle(connectionContext, sqlNode);
    }

    private void defaultStringHandler(String command) {
        List<List<String>> data = Lists.newArrayListWithCapacity(1);
        data.add(Lists.newArrayList("Yuqi version"));

        final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                .columnName(new String[] {"@@VERSION_COMMENT"})
                .columnType(new int[] {0xfd})
                .data(data)
                .schema(StringUtils.EMPTY)
                .table(StringUtils.EMPTY)
                .build();

        final ByteBuf byteBuf = PackageUtils.buildResultSet(resultSetHolder);

        connectionContext.getChannelHandlerContext().channel()
                .writeAndFlush(byteBuf);
        byteBuf.clear();
    }
}
