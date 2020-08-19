package com.yuqi.sql;

import com.google.common.collect.Maps;
import com.yuqi.LifeCycle;
import com.yuqi.protocol.connection.mysql.SchemaMeta;
import org.apache.calcite.jdbc.CalciteSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/7/20 20:19
 **/
public class SlothSchemaHolder implements LifeCycle {

    private Map<String, SlothSchema> schemaMap;

    public static final SlothSchemaHolder INSTANCE = new SlothSchemaHolder();

    public SlothSchemaHolder() {
        this.schemaMap = Maps.newHashMap();
    }

    @Override
    public void init() {

        if (!SchemaMeta.INSTANCE.schemaIsOk()) {
            return;
        }

        //add default schema
        Set<String> schemes = SchemaMeta.INSTANCE.allSchema();
        for (String schema : schemes) {
            SlothSchema slothSchema = registerSchema(schema);
            //register table

            //
        }
    }

    @Override
    public void close() {

    }

    public SlothSchema registerSchema(String schemaName) {

        //add db first
        if (SchemaMeta.INSTANCE.schemaIsOk()) {
            SchemaMeta.INSTANCE.addSchema(schemaName);
        }

        //then add in schema
        final SlothSchema slothSchema = new SlothSchema(schemaName);
        schemaMap.put(schemaName, slothSchema);
        CalciteSchema schema =
                ParserFactory.getCatalogReader().getRootSchema().add(schemaName, slothSchema);
        slothSchema.setSchema(schema);
        return slothSchema;
    }

    public boolean removeSchema(String schemaName) {
        schemaMap.remove(schemaName);

        //remove data in schema
        ParserFactory.getCatalogReader().getRootSchema()
                .removeSubSchema(schemaName);

        //remove data in db;
        if (SchemaMeta.INSTANCE.schemaIsOk()) {
            SchemaMeta.INSTANCE.dropSchema(schemaName);
        }

        return true;
    }

    public List<String> getAllSchemas() {
        return new ArrayList<>(schemaMap.keySet());
    }

    public boolean contains(String db) {
        return schemaMap.containsKey(db);
    }

    public SlothSchema getSlothSchema(String dbName) {
        return schemaMap.get(dbName);
    }

    private void addDefaultSchemasAndTables() {

        //first add information schema
        SlothSchema slothSchema = registerSchema("information_schema");

        //second add inner table in information_schema;

    }
}
