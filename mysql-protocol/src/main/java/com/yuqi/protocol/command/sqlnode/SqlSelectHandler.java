package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.utils.PackageUtils;
import io.netty.buffer.ByteBuf;
import org.apache.calcite.sql.SqlSelect;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 17:03
 **/
public class SqlSelectHandler implements Handler<SqlSelect> {

    public static final SqlSelectHandler INSTANCE = new SqlSelectHandler();

    @Override
    public void handle(ConnectionContext connectionContext, SqlSelect sqlNode) {
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
