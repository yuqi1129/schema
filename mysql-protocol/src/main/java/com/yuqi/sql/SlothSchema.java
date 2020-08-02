package com.yuqi.sql;

import com.google.common.collect.Maps;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 10/7/20 19:52
 **/
public class SlothSchema extends AbstractSchema {

    private Map<String, Table> tables = Maps.newHashMap();
    private String schemaName;

    public SlothSchema(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        return tables;
    }

    private boolean dropTable(String tableName) {
        tables.remove(tableName);

        return true;

        //flush to db in case machine is crash

        //notify other frontEnd that another table is add
        //another front end
    }

    public boolean addTable(String tableName, SlothTable slothTable) {
        tables.put(tableName, slothTable);
        final CalciteSchema calciteSchema = ParserFactory.getCatalogReader()
                .getRootSchema()
                .getSubSchema(schemaName, false);

        calciteSchema.add(tableName, slothTable);
        return true;
    }


    public boolean containsTable(String tableName) {
        return tables.containsKey(tableName);
    }

}
