package com.yuqi.sql;

import com.google.common.collect.Maps;
import org.apache.calcite.jdbc.CalciteSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        init();
    }

    @Override
    public void init() {
        //TODO load schema from file or db
        //add default schema
    }

    @Override
    public void close() {

    }

    public SlothSchema registerSchema(String schemaName) {
        final SlothSchema slothSchema = new SlothSchema(schemaName);
        schemaMap.put(schemaName, slothSchema);
        CalciteSchema schema =
                ParserFactory.getCatalogReader().getRootSchema().add(schemaName, slothSchema);
        slothSchema.setSchema(schema);
        return slothSchema;
        //todo insert into db to store
    }

    public boolean removeSchema(String schemaName) {
        schemaMap.remove(schemaName);
        return ParserFactory.getCatalogReader().getRootSchema().removeSubSchema(schemaName);

        //todo delete schema in persistent store
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
