package com.yuqi.protocol.command.sqlnode;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import com.yuqi.protocol.connection.ConnectionContext;
import com.yuqi.protocol.pkg.MysqlPackage;
import com.yuqi.protocol.utils.PackageUtils;
import com.yuqi.sql.SlothSchema;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.SlothTable;
import com.yuqi.storage.lucene.TableEngine;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static com.yuqi.protocol.constants.ErrorCodeAndMessageEnum.NO_DATABASE_SELECTED;

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

        //
        final String tableAndDb = table.toString();
        String[] dbAndDbArray = tableAndDb.split("\\.", 2);

        String db = dbAndDbArray.length == 2 ? dbAndDbArray[0] : connectionContext.getDb();
        String tableName = dbAndDbArray.length == 2 ? dbAndDbArray[1] : dbAndDbArray[0];

        MysqlPackage r = null;
        if (Objects.isNull(db)) {
            r = PackageUtils.buildErrPackage(
                    NO_DATABASE_SELECTED.getCode(),
                    NO_DATABASE_SELECTED.getMessage(),
                    1);

            connectionContext.write(r);
            return;
        }

        SlothSchema slothSchema = SlothSchemaHolder.INSTANCE.getSlothSchema(db);
        if (Objects.isNull(slothSchema)) {
            r = PackageUtils.buildErrPackage(
                    NO_DATABASE_SELECTED.getCode(),
                    NO_DATABASE_SELECTED.getMessage(),
                    1);
            connectionContext.write(r);
            return;
        }

        SlothTable slothTable = (SlothTable) slothSchema.getTable(tableName);
        TableEngine tableEngine = slothTable.getTableEngine();
        tableEngine.insert(Lists.newArrayList());

        r = PackageUtils.buildOkMySqlPackage(0, 1, 0);

        connectionContext.write(r);
    }

    private List<Value> extractColumnValue(List<SqlNode> rows) {
        //todo

        return null;
    }

}
