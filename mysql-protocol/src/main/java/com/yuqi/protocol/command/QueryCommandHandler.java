package com.yuqi.protocol.command;

import com.yuqi.protocol.command.sqlnode.Handler;
import com.yuqi.protocol.command.sqlnode.HandlerHolder;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.request.Query;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import org.apache.calcite.sql.SqlNode;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 21:28
 **/
public class QueryCommandHandler extends AbstractCommandHandler {
    private Query queryPackage;

    public QueryCommandHandler(ConnectionContext connectionContext, Query queryPackage) {
        super(connectionContext);
        this.queryPackage = queryPackage;
    }

    @Override
    public void execute() {
        //query handler do query
        //1. read sql and hint
        //2. set/select/ect/
        //3. execute
        //4. write result to channel and return
        //mysql first execute select @@version_comment limit 1;
        //

        SqlNode sqlNode = null;
        //RelNode relNode = null;
        try {
            final SlothParser slothParser = ParserFactory.getParser(queryPackage.getQuery());
            sqlNode = slothParser.getSqlNode(queryPackage.getQuery());
            //relNode = slothParser.getPlan(queryPackage.getQuery());
            handleSqlNode(sqlNode);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
            handleSqlString(queryPackage.getQuery());
        }
    }


    private void handleSqlString(String command) {
        //todo
    }

    private void handleSqlNode(SqlNode sqlNode) {
        final Class<?> sqlType = sqlNode.getClass();
        final Handler handler = HandlerHolder.SQL_TYPE_TO_HANDLER_MAP.get(sqlType);
        handler.handle(connectionContext, sqlNode);
    }
}
