package com.yuqi.protocol.utils;

import com.google.common.collect.Lists;
import com.yuqi.protocol.io.ReaderAndWriter;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.pkg.ResultSetHolder;
import com.yuqi.protocol.pkg.auth.ServerGreeting;
import com.yuqi.protocol.pkg.response.ColumnCount;
import com.yuqi.protocol.pkg.response.ColumnType;
import com.yuqi.protocol.pkg.response.ColumnValue;
import com.yuqi.protocol.pkg.response.EofPackage;
import com.yuqi.protocol.pkg.response.ErrPackage;
import com.yuqi.protocol.pkg.response.OkPackage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.List;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.SYNTAX_ERROR;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_CONNECT_WITH_DB;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_FOUND_ROWS;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_IGNORE_SIGPIPE;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_IGNORE_SPACE;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_INTERACTIVE;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_LONG_FLAG;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_LONG_PASSWORD;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_ODBC;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_PLUGIN_AUTH;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_PROTOCOL_41;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_SECURE_CONNECTION;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_TRANSACTIONS;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 2/7/20 20:44
 **/
public class PackageUtils {

    public static byte[] salt1 = {1, 1, 1, 1, 1, 1, 1, 1};
    public static final String AUTHENCATION_PLUGIN = "mysql_native_password";
    private static final int SERVER_VERSION = 0x0a;
    private static final String MYSQL_SERVER_VERSION = "5.7.22";
    public static byte[] salt2 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public static ServerGreeting buildInitAuthencatinPackage() {

        int serverCapability = getServerCapality();
        byte[] sereverCapacility = IOUtils.getBytes(serverCapability);
        byte[] lower = new byte[] {sereverCapacility[0], sereverCapacility[1]};
        byte[] higher = new byte[] {sereverCapacility[2], sereverCapacility[3]};

        ServerGreeting greetingPackage = ServerGreeting.builder()
                .serverThreadId((int) Thread.currentThread().getId())
                .saltOne(salt1)
                .protocalVeriosn((byte) SERVER_VERSION)
                .serverVeriosnInfo(MYSQL_SERVER_VERSION)
                //origin is 0xff, cause only disable-ssl mysql -h127.0.0.1 -P3016 -uroot -p123456 --ssl-mode=disabled can connect
                .serverCapability((short) (serverCapability & 0x0000ffff))
                .extendServerCapabilities((short) ((serverCapability >> 16) & 0x0000ffff))
                //see https://dev.mysql.com/doc/internals/en/character-set.html#packet-Protocol::CharacterSet
                .serverLanguage((byte) 33)
                //see https://dev.mysql.com/doc/internals/en/status-flags.html#packet-Protocol::StatusFlags
                .serverStatus((short) 2)
                .authencationPluginLenth((byte) AUTHENCATION_PLUGIN.length())
                .saltTwo(salt2)
                .authencationPlugin(AUTHENCATION_PLUGIN)
                .build();

        return greetingPackage;
    }

    public static int getServerCapality() {
        int flags = 0;

        flags |= CLIENT_LONG_PASSWORD;
        flags |= CLIENT_FOUND_ROWS;
        flags |= CLIENT_LONG_FLAG;
        flags |= CLIENT_CONNECT_WITH_DB;
        flags |= CLIENT_ODBC;
        flags |= CLIENT_IGNORE_SPACE;
        flags |= CLIENT_PROTOCOL_41;
        flags |= CLIENT_INTERACTIVE;
        flags |= CLIENT_IGNORE_SIGPIPE;
        flags |= CLIENT_TRANSACTIONS;
        flags |= CLIENT_SECURE_CONNECTION;
        flags |= CLIENT_PLUGIN_AUTH;
        return flags;
    }


    public static MysqlPackage buildOkMySqlPackage(int affectedRows, int seqNumber, int lastInsertId) {
        OkPackage okPackage = OkPackage.builder()
                .header((byte) 0x00)
                .serverStatus(0x0002)
                .affectedRows(affectedRows)
                .lastInsertId(lastInsertId)
                .build();

        MysqlPackage mysqlPacakge = new MysqlPackage(okPackage);
        mysqlPacakge.setSeqNumber((byte) seqNumber);

        return mysqlPacakge;
    }

    public static MysqlPackage buildErrPackage(int errorCode, String errorMessage) {
        return buildErrPackage(errorCode, errorMessage, 1);
    }

    public static MysqlPackage buildErrPackage(int errorCode, String errorMessage, int seqNumber) {
        ErrPackage errPackage = ErrPackage.builder()
                .header((byte) 0xff)
                .errorCode((short) errorCode)
                .errorMessage(errorMessage)
                .build();

        MysqlPackage mysqlPacakge = new MysqlPackage(errPackage);
        mysqlPacakge.setSeqNumber((byte) seqNumber);
        return mysqlPacakge;
    }


    public static MysqlPackage buildSyntaxErrPackage(String query) {
        return buildErrPackage(SYNTAX_ERROR.getCode(), String.format(SYNTAX_ERROR.getMessage(), query), 1);
    }

    public static ByteBuf packageToBuf(ReaderAndWriter readerAndWriter) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(128);
        readerAndWriter.write(byteBuf);

        return byteBuf;
    }

    public static ByteBuf packageToBuf(ReaderAndWriter readerAndWriter, ByteBuf byteBuf) {
        readerAndWriter.write(byteBuf);
        return byteBuf;
    }

    /**
     * 构造ResultSet包
     * @param resultSetHolder
     * @return
     */
    public static ByteBuf buildResultSet(ResultSetHolder resultSetHolder) {
        final List<MysqlPackage> result = Lists.newArrayList();

        final List<List<String>> data = resultSetHolder.getData();
        final List<Integer> columnType = resultSetHolder.getColumnType();
        final String[] columnName = resultSetHolder.getColumnName();

        final int columnNum = columnType.size();

        //query seqNumber is 0 so result query number from 1
        byte seqNumber = 1;

        //first is the column count
        final MysqlPackage columnCountPackage = new MysqlPackage(
                new ColumnCount(columnNum));
        columnCountPackage.setSeqNumber(seqNumber++);
        result.add(columnCountPackage);

        //second is the column detail
        List<MysqlPackage> columnDetails = Lists.newArrayList();

        for (int i = 0; i < columnNum; i++) {
            final MysqlPackage columnTypeMysqlPackage = new MysqlPackage();
            final ColumnType columnTypePackage =
                    ColumnType.builder()
                            .catalog("def")
                            .schema(resultSetHolder.getSchema())
                            .table(resultSetHolder.getTable())
                            .orgTable("")
                            .name(columnName[i])
                            .originalName("")
                            //original 33
                            .charSet(0)
                            .filler((byte) 0x0c)
                            //original 84
                            .columnLength(0)
                            .columnType((byte) columnType.get(i).intValue())
                            .flags(0x00)
                            .dicimals((byte) 0x00)
                            .build();

            columnTypeMysqlPackage.setAbstractReaderAndWriterPackage(columnTypePackage);
            columnTypeMysqlPackage.setSeqNumber(seqNumber++);
            columnDetails.add(columnTypeMysqlPackage);
        }
        result.addAll(columnDetails);

        //third is end of package
        final MysqlPackage eofPackage = new MysqlPackage(new EofPackage((byte) 0xfe, 0, 0x0002));
        eofPackage.setSeqNumber(seqNumber++);
        result.add(eofPackage);

        //fourth is row value
        List<MysqlPackage> rows = Lists.newArrayList();
        for (List<String> row : data) {
            MysqlPackage columnValue = new MysqlPackage(new ColumnValue(row));
            columnValue.setSeqNumber(seqNumber++);
            rows.add(columnValue);
        }
        result.addAll(rows);

        //eof package again
        final MysqlPackage eofPackageLast = new MysqlPackage(new EofPackage((byte) 0xfe, 0, 0x0002));
        eofPackageLast.setSeqNumber(seqNumber++);
        result.add(eofPackageLast);


        //write to buffer
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(128);
        result.forEach(a -> a.write(buf));

        return buf;
    }
}
