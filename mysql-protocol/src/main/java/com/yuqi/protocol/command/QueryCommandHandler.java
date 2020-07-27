package com.yuqi.protocol.command;

import com.google.common.collect.Lists;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MySQLPackage;
import com.yuqi.protocol.pkg.request.QueryPackage;
import com.yuqi.protocol.pkg.response.ColumnCountPackage;
import com.yuqi.protocol.pkg.response.ColumnTypePackage;
import com.yuqi.protocol.pkg.response.ColumnValuePackage;
import com.yuqi.protocol.pkg.response.EofPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.ParserFactory;
import com.yuqi.sql.SlothParser;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.ddl.SqlCreateDb;
import com.yuqi.sql.ddl.SqlDropDb;
import com.yuqi.sql.ddl.SqlShow;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
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

        SqlNode sqlNode = null;
        try {
            final SlothParser slothParser = ParserFactory.getParser(queryPackage.getQuery());
            sqlNode = slothParser.getSqlNode();
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

            MySQLPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(1, 1, 0);
            ByteBuf byteBuf = PackageUtils.packageToBuf(mysqlPackage);
            connectionContext.getChannelHandlerContext().writeAndFlush(byteBuf);

        } else if (sqlNode instanceof SqlDropDb) {
            //handle sql create db;
            final SqlDropDb createDb = (SqlDropDb) sqlNode;
            final String db = createDb.getDbName();
            SlothSchemaHolder.INSTANCE.removeSchema(db);

            MySQLPackage mysqlPackage = PackageUtils.buildOkMySqlPackage(1, 1, 0);
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
            for (String db : schemas) {
                MySQLPackage columnValue = new MySQLPackage(new ColumnValuePackage(Lists.newArrayList(db)));
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


        } else {
            //sql select
            final List<MySQLPackage> result = Lists.newArrayList();
            int columnNum = 1;
            byte seqNumber = 1;

            //列count
            final MySQLPackage columnCount = new MySQLPackage(
                    new ColumnCountPackage(columnNum));
            columnCount.setSeqNumber(seqNumber++);

            result.add(columnCount);

            //列详情
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

            //eof
            final MySQLPackage eofPackage = new MySQLPackage(new EofPackage((byte) 0xfe, 0, 0x0002));
            eofPackage.setSeqNumber(seqNumber++);

            result.add(eofPackage);

            //每一行数据
            int rowCount = 10;
            List<MySQLPackage> rows = Lists.newArrayList();
            for (int i = 0; i < rowCount; i++) {
                MySQLPackage columnValue = new MySQLPackage(new ColumnValuePackage(Lists.newArrayList("Yuqi version")));
                columnValue.setSeqNumber(seqNumber++);
                rows.add(columnValue);
            }

            result.addAll(rows);
            //eof
            final MySQLPackage eofPackageLast = new MySQLPackage(new EofPackage((byte) 0xfe, 0, 0x0002));
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
