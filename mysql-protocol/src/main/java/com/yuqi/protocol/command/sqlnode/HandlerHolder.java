package com.yuqi.protocol.command.sqlnode;


import com.yuqi.sql.sqlnode.ddl.SqlCreateDb;
import com.yuqi.sql.sqlnode.ddl.SqlCreateTable;
import com.yuqi.sql.sqlnode.ddl.SqlDrop;
import com.yuqi.sql.sqlnode.ddl.SqlSet;
import com.yuqi.sql.sqlnode.ddl.SqlShow;
import com.yuqi.sql.sqlnode.ddl.SqlUse;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlOrderBy;
import org.apache.calcite.sql.SqlSelect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 31/7/20 17:08
 **/
public class HandlerHolder {

    public static final Map<Class<?>, Handler<?>> SQL_TYPE_TO_HANDLER_MAP = new HashMap<>();

    static {
        SQL_TYPE_TO_HANDLER_MAP.put(SqlUse.class, SqlUseHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlShow.class, SqlShowHandler.INSTANCE);

        SQL_TYPE_TO_HANDLER_MAP.put(SqlSelect.class, SqlSelectHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlOrderBy.class, SqlSelectHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlBasicCall.class, SqlSelectHandler.INSTANCE);

        SQL_TYPE_TO_HANDLER_MAP.put(SqlDrop.class, SqlDropHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlCreateDb.class, SqlCreateDbHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlCreateTable.class, SqlCreateTableHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlSet.class, SqlSetHandler.INSTANCE);

        SQL_TYPE_TO_HANDLER_MAP.put(SqlInsert.class, SqlInsertHandler.INSTANCE);
    }
}
