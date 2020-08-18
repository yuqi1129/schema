package com.yuqi.protocol.command;

import com.google.common.base.Throwables;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.yuqi.protocol.command.sepcial.RawHandlerHolder.getRawHandler;
import static com.yuqi.protocol.constants.ColumnTypeConstants.MYSQL_TYPE_VAR_STRING;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 4/7/20 21:28
 **/
public class QueryCommandHandler extends AbstractCommandHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(QueryCommandHandler.class);

    private String query;

    public QueryCommandHandler(ConnectionContext connectionContext, String query) {
        super(connectionContext);
        this.query = query;
    }

    @Override
    public void execute() {
        SqlNode sqlNode;
        try {
            //TODO 出现了粘包与粘包现象，明天看看怎么解决
            final SlothParser slothParser = ParserFactory.getParser(query, connectionContext.getDb());
            connectionContext.setQueryString(query);

            //first use raw, for example 'explain'
            Handler handler = getRawHandler(query);
            if (Objects.nonNull(handler)) {
                handler.handle(connectionContext, query);
                return;
            }

            //then parser and use sqlnode
            sqlNode = slothParser.getSqlNode();
            handleSqlNode(sqlNode);
        } catch (Exception e) {
            //TODO 这里异常需要分类处理一下
            LOGGER.error("execute sql\n '{}' \nget error:\n", query, Throwables.getStackTraceAsString(e));

            if (query.contains("@@")) {
                handleSqlString(query);
                return;
            }

            connectionContext.write(PackageUtils.buildSyntaxErrPackage(e.getMessage()));
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

        try {
            handler.handle(connectionContext, sqlNode);
        } catch (Throwable e) {
            LOGGER.error("Execute sql '{}' get error: {}",
                    connectionContext.getQueryString(),
                    Throwables.getStackTraceAsString(e)
            );

            connectionContext.write(PackageUtils.buildErrPackage(-1, e.toString(), 1));
        }
    }

    private void defaultStringHandler(String command) {
        final List<List<String>> data = Lists.newArrayListWithCapacity(1);
        data.add(Lists.newArrayList("Yuqi version"));

        final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                .columnName(new String[] {"@@VERSION_COMMENT"})
                .columnType(Lists.newArrayList(MYSQL_TYPE_VAR_STRING))
                .data(data)
                .schema(StringUtils.EMPTY)
                .table(StringUtils.EMPTY)
                .build();

        final ByteBuf byteBuf = PackageUtils.buildResultSet(resultSetHolder);
        connectionContext.write(byteBuf);
    }
}
