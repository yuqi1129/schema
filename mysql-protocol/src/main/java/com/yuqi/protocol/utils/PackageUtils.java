package com.yuqi.protocol.utils;

import com.yuqi.protocol.io.PackageReaderAndWriter;
import com.yuqi.protocol.pkg.MySQLPackage;
import com.yuqi.protocol.pkg.auth.ServerGreetingPackage;
import com.yuqi.protocol.pkg.response.OkPackage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_CONNECT_WITH_DB;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_FOUND_ROWS;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_IGNORE_SIGPIPE;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_IGNORE_SPACE;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_INTERACTIVE;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_LONG_FLAG;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_LONG_PASSWORD;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_ODBC;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_PROTOCOL_41;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_SECURE_CONNECTION;
import static com.yuqi.protocol.constants.ServerCapabilityFlags.CLIENT_TRANSACTIONS;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 2/7/20 20:44
 **/
public class PackageUtils {

    public static byte[] salt1 = {1, 1, 1, 1, 1, 1, 1, 1};
    public static byte[] salt2 = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    public static ServerGreetingPackage buildInitAuthencatinPackage() {

        int serverCapability = getServerCapality();
        ServerGreetingPackage greetingPackage = ServerGreetingPackage.builder()
                .serverThreadId((int) Thread.currentThread().getId())
                .saltOne(salt1)
                .protocalVeriosn((byte) 10)
                .serverVeriosnInfo("5.7.22")
                //origin is 0xff, cause only disable-ssl mysql -h127.0.0.1 -P3016 -uroot -p123456 --ssl-mode=disabled can connect
                .serverCapability((short) (serverCapability & 0x0000ffff))
                //编码方式, 后面主键完善
                .serverLanguage((byte) 33)
                .serverStatus((short) 2)
                .extendServerCapabilities((short) ((serverCapability >> 16) & 0x0000ffff))
                .authencationPluginLenth((byte) 0x15)
                .saltTwo(salt2)
                .authencationPlugin("mysql_native_password")
                .build();

        return greetingPackage;
    }

    private static int getServerCapality() {
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
        return flags;
    }


    public static MySQLPackage buildOkMySqlPackage(int affectedRows, int seqNumber, int lastInsertId) {
        OkPackage okPackage = OkPackage.builder()
                .header((byte) 0x00)
                .serverStatus(0x0002)
                .affectedRows(affectedRows)
                .lastInsertId(lastInsertId)
                .build();

        MySQLPackage mysqlPacakge = new MySQLPackage(okPackage);
        mysqlPacakge.setSeqNumber((byte) seqNumber);

        return mysqlPacakge;
    }

    public static MySQLPackage buildResultSetMysqlPackage() {


        return null;
    }

    public static ByteBuf packageToBuf(PackageReaderAndWriter packageReaderAndWriter) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer(128);
        packageReaderAndWriter.write(byteBuf);

        return byteBuf;
    }

    public static ByteBuf packageToBuf(PackageReaderAndWriter packageReaderAndWriter, ByteBuf byteBuf) {
        packageReaderAndWriter.write(byteBuf);
        return byteBuf;
    }
}
