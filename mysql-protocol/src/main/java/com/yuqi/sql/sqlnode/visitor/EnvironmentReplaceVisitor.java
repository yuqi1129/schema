package com.yuqi.sql.sqlnode.visitor;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.netty.ConnectionContext;
import com.yuqi.sql.env.SlothEnvironmentValueHolder;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.util.SqlShuttle;

import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/9/20 16:07
 **/
public class EnvironmentReplaceVisitor extends SqlShuttle {

    private ConnectionContext connectionContext;

    public EnvironmentReplaceVisitor(ConnectionContext connectionContext) {
        this.connectionContext = connectionContext;
    }

    public ConnectionContext getConnectionContext() {
        return connectionContext;
    }

    @Override
    public SqlNode visit(SqlIdentifier id) {
        final String name = id.toString();

        if (name.startsWith("@@")) {
            final String key = name.substring(2);

            String value = connectionContext.getProperties().get(key);
            if (Objects.isNull(value)) {
                value = SlothEnvironmentValueHolder.INSTACNE.propertyValue(key);
                if (Objects.isNull(value)) {
                    throw new UnsupportedOperationException("Can't suppport environment value '" + key + "'");
                }
            }

            final SqlLiteral environmentValue =
                    SqlLiteral.createCharString(value, null, SqlParserPos.ZERO);

            SqlIdentifier sqlIdentifier = new SqlIdentifier(Lists.newArrayList(key), SqlParserPos.ZERO);
            return SqlStdOperatorTable.AS.createCall(SqlParserPos.ZERO, environmentValue, sqlIdentifier);
        }

        return super.visit(id);
    }
}
