package com.yuqi.protocol.command.sqlnode;

import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 9/8/20 21:03
 **/
public class SqlInsertHandler implements Handler<SqlInsert> {

    public static final SqlInsertHandler INSTANCE = new SqlInsertHandler();
    public static final Logger LOGGER = LoggerFactory.getLogger(SqlInsertHandler.class);

    @Override
    public void handle(ConnectionContext connectionContext, SqlInsert type) {

        //暂时就支持简单的insert into xx values(...)
        //insert into xxx select xxx ... 这个后面再支持
        LOGGER.info(type.toString());

        //judge table is exist and column
        //judge column size and data size;
        //resolve column value
        //insert, make a mapping between db_table <---> tableEngine

        final SqlNode table = type.getTargetTable();
        final SqlNode columnList = type.getTargetColumnList();
        final List<SqlNode> operands = ((SqlBasicCall) type.getSource()).getOperandList();

        MysqlPackage r =
                PackageUtils.buildOkMySqlPackage(0, 1, 0);

        connectionContext.write(r);
    }
}
