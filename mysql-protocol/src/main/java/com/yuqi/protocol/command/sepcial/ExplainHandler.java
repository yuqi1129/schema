package com.yuqi.protocol.command.sepcial;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.protocol.command.sqlnode.Handler;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import io.netty.buffer.ByteBuf;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlExplainLevel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 3/8/20 20:10
 **/
public class ExplainHandler implements Handler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainHandler.class);

    public static final ExplainHandler INSTANCE = new ExplainHandler();

    @Override
    public void handle(ConnectionContext connectionContext, String type) {
        final String query = type.split(" ", 2)[1];
        final SlothParser slothParser = ParserFactory.getParser(query, connectionContext.getDb());

        ByteBuf result;
        try {
            final RelNode relNode = slothParser.getPlan(query);
            final String planString = "\n" + RelOptUtil.toString(relNode, SqlExplainLevel.ALL_ATTRIBUTES);
            final List<List<String>> data = Lists.newArrayList();
            data.add(Lists.newArrayList(planString));

            final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                    .table(StringUtils.EMPTY)
                    .columnType(Lists.newArrayList(0xfd))
                    .schema(StringUtils.EMPTY)
                    .data(data)
                    .columnName(new String[]{"Plan"})
                    .build();

            result = PackageUtils.buildResultSet(resultSetHolder);
        } catch (Throwable e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
            result = PackageUtils.packageToBuf(PackageUtils.buildSyntaxErrPackage(query));
        }

        connectionContext.write(result);
    }
}
