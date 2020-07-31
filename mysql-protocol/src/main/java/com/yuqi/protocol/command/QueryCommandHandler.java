package com.yuqi.protocol.command;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MySQL;
import com.yuqi.protocol.pkg.request.Query;
import com.yuqi.protocol.pkg.response.ColumnCount;
import com.yuqi.protocol.pkg.response.ColumnType;
import com.yuqi.protocol.pkg.response.ColumnValue;
import com.yuqi.protocol.pkg.response.Eof;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlCreateDb;
import com.yuqi.sql.ddl.SqlDropDb;
import com.yuqi.sql.ddl.SqlShow;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.SqlNode;

import java.util.Collections;
import java.util.List;

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
        RelNode relNode = null;
        try {
            final SlothParser slothParser = ParserFactory.getParser(queryPackage.getQuery());
            sqlNode = slothParser.getSqlNode(queryPackage.getQuery());
            relNode = slothParser.getPlan(queryPackage.getQuery());
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }

        handleSqlNode(sqlNode);
    }

    private void handleSqlNode(SqlNode sqlNode) {
        //
        if (sqlNode instanceof SqlCreateDb) {
            //handle sql create db;
            final SqlCreateDb createDb = (SqlCreateDb) sqlNode;
            final String db = createDb.getDbName();
            SlothSchemaHolder.INSTANCE.registerSchema(db);

            MySQL mysqlPackage = PackageUtils.buildOkMySqlPackage(1, 1, 0);
            ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
            connectionContext.getChannelHandlerContext().writeAndFlush(byteBuf);

        } else if (sqlNode instanceof SqlDropDb) {
            //handle sql create db;
            final SqlDropDb createDb = (SqlDropDb) sqlNode;
            final String db = createDb.getDbName();
            SlothSchemaHolder.INSTANCE.removeSchema(db);

            MySQL mysqlPackage = PackageUtils.buildOkMySqlPackage(1, 1, 0);
            ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
            connectionContext.getChannelHandlerContext().writeAndFlush(byteBuf);

        } else if (sqlNode instanceof SqlShow) {
            //
            SqlShow sqlShow = (SqlShow) sqlNode;
            final String command = sqlShow.getCommand();

            //todo
            List<String> schemas = Collections.EMPTY_LIST;
            if ("databases".equalsIgnoreCase(command)) {
                schemas = SlothSchemaHolder.INSTANCE.getAllSchemas();
            }

            final List<MySQL> result = Lists.newArrayList();
            int columnNum = 1;
            byte seqNumber = 1;
            final MySQL columnCount = new MySQL(
                    new ColumnCount(columnNum));
            columnCount.setSeqNumber(seqNumber++);

            result.add(columnCount);

            List<MySQL> columnDetails = Lists.newArrayList();

            for (int i = 0; i < columnNum; i++) {
                final MySQL columnType = new MySQL();

                //todo file column type info
                final ColumnType columnTypePackage =
                        ColumnType.builder()
                                .catalog("def")
                                .schema("")
                                .table("")
                                .orgTable("")
                                .name("Database")
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

                columnType.setAbstractReaderAndWriterPackage(columnTypePackage);
                columnType.setSeqNumber(seqNumber++);

                columnDetails.add(columnType);
            }

            result.addAll(columnDetails);

            final MySQL eofPackage = new MySQL(new Eof((byte) 0xfe, 0, 0x0002));
            eofPackage.setSeqNumber(seqNumber++);

            result.add(eofPackage);

            int rowCount = 10;
            List<MySQL> rows = Lists.newArrayList();
            for (String db : schemas) {
                MySQL columnValue = new MySQL(new ColumnValue(Lists.newArrayList(db)));
                columnValue.setSeqNumber(seqNumber++);
                rows.add(columnValue);
            }

            result.addAll(rows);

            final MySQL eofPackageLast = new MySQL(new Eof((byte) 0xfe, 0, 0x0002));
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


        } else {
            //sql select
            final List<MySQL> result = Lists.newArrayList();
            int columnNum = 1;
            byte seqNumber = 1;

            //列count
            final MySQL columnCount = new MySQL(
                    new ColumnCount(columnNum));
            columnCount.setSeqNumber(seqNumber++);

            result.add(columnCount);

            //列详情
            List<MySQL> columnDetails = Lists.newArrayList();
            for (int i = 0; i < columnNum; i++) {
                final MySQL columnType = new MySQL();

                //todo file column type info
                final ColumnType columnTypePackage =
                        ColumnType.builder()
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

                columnType.setAbstractReaderAndWriterPackage(columnTypePackage);
                columnType.setSeqNumber(seqNumber++);

                columnDetails.add(columnType);
            }

            result.addAll(columnDetails);

            //eof
            final MySQL eofPackage = new MySQL(new Eof((byte) 0xfe, 0, 0x0002));
            eofPackage.setSeqNumber(seqNumber++);

            result.add(eofPackage);

            //每一行数据
            int rowCount = 10;
            List<MySQL> rows = Lists.newArrayList();
            for (int i = 0; i < rowCount; i++) {
                MySQL columnValue = new MySQL(new ColumnValue(Lists.newArrayList("Yuqi version")));
                columnValue.setSeqNumber(seqNumber++);
                rows.add(columnValue);
            }

            result.addAll(rows);
            //eof
            final MySQL eofPackageLast = new MySQL(new Eof((byte) 0xfe, 0, 0x0002));
            eofPackageLast.setSeqNumber(seqNumber++);
            result.add(eofPackageLast);


            //flush to channel
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(128);
            result.forEach(a -> a.write(buf));

            connectionContext.getChannelHandlerContext().channel()
                    .writeAndFlush(buf);
            buf.clear();
        }
    }
}
