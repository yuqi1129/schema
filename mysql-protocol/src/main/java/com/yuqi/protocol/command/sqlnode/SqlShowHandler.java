package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlShow;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 17:00
 **/
public class SqlShowHandler implements Handler<SqlShow> {

    public static final SqlShowHandler INSTANCE = new SqlShowHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlShow sqlNode) {
        final String command = sqlNode.getCommand();
        //todo
        List<String> schemas = Collections.EMPTY_LIST;
        if ("databases".equalsIgnoreCase(command)) {
            schemas = SlothSchemaHolder.INSTANCE.getAllSchemas();
        }

        final ResultSetHolder resultSetHolder = ResultSetHolder.builder()
                .columnName(new String[] {"Database"})
                .columnType(new int[] {0xfd})
                .data(schemas.stream().map(Lists::newArrayList).collect(Collectors.toList()))
                .schema(StringUtils.EMPTY)
                .table(StringUtils.EMPTY)
                .build();

        final ByteBuf byteBuf = PackageUtils.buildResultSet(resultSetHolder);

        connectionContext.getChannelHandlerContext().channel()
                .writeAndFlush(byteBuf);
        byteBuf.clear();
    }
}
