package com.yuqi.sql;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
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
        //load schema from file or db
    }

    @Override
    public void close() {

    }

    public void registerSchema(String schemaName) {
        final SlothSchema slothSchema = new SlothSchema(schemaName);
        schemaMap.put(schemaName, slothSchema);
        ParserFactory.getCatalogReader().getRootSchema().add(schemaName, slothSchema);

        //insert into db to store
    }

    public void removeSchema(String schemaName) {
        schemaMap.remove(schemaName);
        ParserFactory.getCatalogReader().getRootSchema().removeSubSchema(schemaName);

        //delete schema in persistent store
    }

    public List<String> getAllSchemas() {
        return new ArrayList<>(schemaMap.keySet());
    }
}
