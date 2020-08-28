package com.yuqi.protocol.command.sepcial;


import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.constants.ColumnTypeConstants;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 18:46
 **/
public class CurrentDatabaseHandler extends BaseHandler {
    public static final CurrentDatabaseHandler INSTANCE = new CurrentDatabaseHandler();

    public static final String CURRENT_DATABASE_RESULT_COLUMN = "database()";
    @Override
    public void handle(ConnectionContext connectionContext, String type) {
        String db = connectionContext.getDb();

        final List<List<String>> data = Lists.newArrayList();
        data.add(Lists.newArrayList(db));

        final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                .columnName(new String[] {CURRENT_DATABASE_RESULT_COLUMN})
                .columnType(Lists.newArrayList(ColumnTypeConstants.MYSQL_TYPE_VAR_STRING))
                .data(data)
                .schema(StringUtils.EMPTY)
                .table(StringUtils.EMPTY)
                .build();

        final ByteBuf byteBuf = PackageUtils.buildResultSet(resultSetHolder);
        connectionContext.write(byteBuf);
    }
}
