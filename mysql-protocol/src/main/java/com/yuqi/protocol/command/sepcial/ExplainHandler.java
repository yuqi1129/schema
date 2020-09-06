package com.yuqi.protocol.command.sepcial;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.constant.StringConstants;
import com.yuqi.protocol.command.sqlnode.Handler;
import com.yuqi.protocol.connection.netty.ConnectionContext;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import com.yuqi.sql.sqlnode.visitor.EnvironmentReplaceVisitor;
import io.netty.buffer.ByteBuf;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlExplainLevel;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.yuqi.protocol.constants.ColumnTypeConstants.MYSQL_TYPE_VAR_STRING;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 3/8/20 20:10
 **/
public class ExplainHandler implements Handler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainHandler.class);
    public static final ExplainHandler INSTANCE = new ExplainHandler();

    private static final String EXPLAIN_RESULT_COLUMN = "Plan";
    @Override
    public void handle(ConnectionContext connectionContext, String type) {

        final String query = type.split(StringConstants.SPACE, 2)[1];
        final SlothParser slothParser = ParserFactory.getParser(query, connectionContext.getDb());

        ByteBuf result;
        try {
            final SqlNode sqlNode = slothParser.getSqlNode(query);
            final RelNode relNode = slothParser.getPlan(sqlNode.accept(new EnvironmentReplaceVisitor(connectionContext)));
            final String planString = StringConstants.LINE_SEPARATOR + RelOptUtil.toString(relNode, SqlExplainLevel.ALL_ATTRIBUTES);
            final List<List<String>> data = Lists.newArrayList();
            data.add(Lists.newArrayList(planString));

            final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                    .table(StringUtils.EMPTY)
                    .columnType(Lists.newArrayList(MYSQL_TYPE_VAR_STRING))
                    .schema(StringUtils.EMPTY)
                    .data(data)
                    .columnName(new String[]{EXPLAIN_RESULT_COLUMN})
                    .build();

            result = PackageUtils.buildResultSet(resultSetHolder);
        } catch (Throwable e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
            result = PackageUtils.packageToBuf(PackageUtils.buildSyntaxErrPackage(query));
        }

        connectionContext.write(result);
    }
}
