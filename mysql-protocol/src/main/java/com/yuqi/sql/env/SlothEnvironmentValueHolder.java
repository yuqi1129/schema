package com.yuqi.sql.env;

import com.google.common.collect.Maps;
import com.yuqi.LifeCycle;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 6/9/20 15:50
 **/
public class SlothEnvironmentValueHolder implements LifeCycle {

    public static final SlothEnvironmentValueHolder INSTACNE
            = new SlothEnvironmentValueHolder();

    private Map<String, String> properties;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void add(String key, String value) {
        properties.put(key, value);
    }

    public String propertyValue(String key) {
        return properties.get(key);
    }

    @Override
    public void init() {
        //load data from constants
        properties = Maps.newHashMap();
        properties.putAll(EnvironmentValues.GLOBAL_ENVIRONMENT);
    }

    @Override
    public void start() {
        //Currently do nothing, try to load data from disk
        //TODO store enviroment values in database
    }

    @Override
    public void close() {

    }
}
