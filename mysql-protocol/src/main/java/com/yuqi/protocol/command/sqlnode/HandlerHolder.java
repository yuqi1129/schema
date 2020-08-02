package com.yuqi.protocol.command.sqlnode;


import com.yuqi.sql.ddl.SqlCreateDb;
import com.yuqi.sql.ddl.SqlCreateTable;
import com.yuqi.sql.ddl.SqlDropDb;
import com.yuqi.sql.ddl.SqlShow;
import com.yuqi.sql.ddl.SqlUse;
import org.apache.calcite.sql.SqlSelect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 31/7/20 17:08
 **/
public class HandlerHolder {

    public static final Map<Class<?>, Handler<?>> SQL_TYPE_TO_HANDLER_MAP = new HashMap<>();

    static {
        SQL_TYPE_TO_HANDLER_MAP.put(SqlUse.class, SqlUseHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlShow.class, SqlShowHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlSelect.class, SqlSelectHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlDropDb.class, SqlDropDbHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlCreateDb.class, SqlCreateDbHandler.INSTANCE);
        SQL_TYPE_TO_HANDLER_MAP.put(SqlCreateTable.class, SqlCreateTableHandler.INSTANCE);
    }
}
