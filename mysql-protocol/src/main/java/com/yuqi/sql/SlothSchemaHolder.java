package com.yuqi.sql;

import com.google.common.collect.Maps;

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
}
