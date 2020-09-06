package com.yuqi.sql.sqlnode.visitor;

import com.google.common.collect.Lists;
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
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 6/9/20 16:07
 **/
public class EnvironmentReplaceVisitor extends SqlShuttle {

    public static final EnvironmentReplaceVisitor INSTANCE
            = new EnvironmentReplaceVisitor();

    @Override
    public SqlNode visit(SqlIdentifier id) {
        final String name = id.toString();

        if (name.startsWith("@@")) {
            final String key = name.substring(2);
            final String value = SlothEnvironmentValueHolder.INSTACNE.propertyValue(key);

            if (Objects.isNull(value)) {
                throw new UnsupportedOperationException("Can't suppport environment value '" + key + "'");
            }

            final SqlLiteral environmentValue =
                    SqlLiteral.createCharString(value, null, SqlParserPos.ZERO);

            SqlIdentifier sqlIdentifier = new SqlIdentifier(Lists.newArrayList(key), SqlParserPos.ZERO);
            return SqlStdOperatorTable.AS.createCall(SqlParserPos.ZERO, environmentValue, sqlIdentifier);
        }

        return super.visit(id);
    }
}
