package com.yuqi.sql;

import com.google.common.collect.Maps;
import com.yuqi.protocol.connection.mysql.TableMeta;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
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

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    protected Map<String, Table> getTableMap() {
        return tables;
    }

    public boolean dropTable(String tableName) {
        tables.remove(tableName);
        calciteSchema.removeTable(tableName);

        TableMeta.INSTANCE.deleteTable(schemaName, tableName);
        return true;
    }

    public boolean addTable(String tableName, SlothTable slothTable) {
        TableMeta.INSTANCE.addTable(schemaName, slothTable);
        tables.put(tableName, slothTable);
        calciteSchema.add(tableName, slothTable);
        return true;
    }

    public void restoreFromDb(SlothTable slothTable) {
        final String tableName = slothTable.getTableName();

        tables.put(tableName, slothTable);
        calciteSchema.add(tableName, slothTable);
    }


    public Collection<Table> getAllTable() {
        return tables.values();
    }

    public boolean containsTable(String tableName) {
        return tables.containsKey(tableName);
    }

    public List<String> getTables() {
        return new ArrayList<>(tables.keySet());
    }

}
