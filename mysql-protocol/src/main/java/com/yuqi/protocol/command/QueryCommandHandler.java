package com.yuqi.protocol.command;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MySQLPackage;
import com.yuqi.protocol.pkg.request.QueryPackage;
import com.yuqi.protocol.pkg.response.ColumnCountPackage;
import com.yuqi.protocol.pkg.response.ColumnTypePackage;
import com.yuqi.protocol.pkg.response.ColumnValuePackage;
import com.yuqi.protocol.pkg.response.EofPackage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 21:28
 **/
public class QueryCommandHandler extends AbstractCommandHandler {
    private QueryPackage queryPackage;

    public QueryCommandHandler(ConnectionContext connectionContext, QueryPackage queryPackage) {
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

        final List<MySQLPackage> result = Lists.newArrayList();
        int columnNum = 1;
        byte seqNumber = 1;
        final MySQLPackage columnCount = new MySQLPackage(
                new ColumnCountPackage(columnNum));
        columnCount.setSeqNumber(seqNumber++);

        result.add(columnCount);

        List<MySQLPackage> columnDetails = Lists.newArrayList();

        for (int i = 0; i < columnNum; i++) {
            final MySQLPackage columnType = new MySQLPackage();

            //todo file column type info
            final ColumnTypePackage columnTypePackage =
                    ColumnTypePackage.builder()
                            .catalog("def")
                            .schema("")
                            .table("")
                            .orgTable("")
                            .name("@@VERSION_COMMENT")
                            .originalName("")
                            //original 33
                            .charSet(0)
                            .filler((byte) 0x0c)
                            //original 84
                            .columnLength(0)
                            .columnType((byte) 0xfd)
                            .flags(0x00)
                            .dicimals((byte) 0x00)
                            .build();

            columnType.setAbstractPackage(columnTypePackage);
            columnType.setSeqNumber(seqNumber++);

            columnDetails.add(columnType);
        }

        result.addAll(columnDetails);

        final MySQLPackage eofPackage = new MySQLPackage(new EofPackage((byte) 0xfe, 0, 0x0002));
        eofPackage.setSeqNumber(seqNumber++);

        result.add(eofPackage);

        int rowCount = 10;
        List<MySQLPackage> rows = Lists.newArrayList();
        for (int i = 0; i < rowCount; i++) {
            MySQLPackage columnValue = new MySQLPackage(new ColumnValuePackage(Lists.newArrayList("Yuqi version")));
            columnValue.setSeqNumber(seqNumber++);
            rows.add(columnValue);
        }

        result.addAll(rows);

        final MySQLPackage eofPackageLast = new MySQLPackage(new EofPackage((byte) 0xfe, 0, 0x0002));
        eofPackageLast.setSeqNumber(seqNumber++);
        result.add(eofPackageLast);


        //flush to channel
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(128);
        result.forEach(a -> a.write(buf));

//        byte[] value = new byte[buf.readableBytes()];
//        buf.readBytes(value);


        connectionContext.getChannelHandlerContext().channel()
                .writeAndFlush(buf);
        buf.clear();
    }
}
