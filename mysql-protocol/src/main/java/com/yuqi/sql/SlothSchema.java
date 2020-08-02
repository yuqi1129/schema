package com.yuqi.sql;

import com.google.common.collect.Maps;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.ArrayList;
import java.util.List;
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
    private CalciteSchema calciteSchema;

    public SlothSchema(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setSchema(CalciteSchema schema) {
        this.calciteSchema = schema;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        return tables;
    }

    public boolean dropTable(String tableName) {
        tables.remove(tableName);
        calciteSchema.removeTable(tableName);

        return true;

        //todo
        //flush to db in case machine is crash
        //notify other frontEnd that another table is add
        //another front end
    }

    public boolean addTable(String tableName, SlothTable slothTable) {
        tables.put(tableName, slothTable);
        calciteSchema.add(tableName, slothTable);
        return true;

        //todo
    }


    public boolean containsTable(String tableName) {
        return tables.containsKey(tableName);
    }

    public List<String> getTables() {
        return new ArrayList<>(tables.keySet());
    }

}
