package com.yuqi;

import com.google.common.collect.Lists;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.env.SlothEnvironmentValueHolder;
import com.yuqi.storage.lucene.StorageService;

import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 18/8/20 17:28
 **/
public class LifeCycleInstance {
    private static List<LifeCycle> lifeCycles = Lists.newArrayList();

    static {
        lifeCycles.add(SlothSchemaHolder.INSTANCE);
        lifeCycles.add(SlothEnvironmentValueHolder.INSTACNE);
        lifeCycles.add(StorageService.INSTANCE);
    }

    public static void add(LifeCycle lifeCycle) {
        lifeCycles.add(lifeCycle);
    }

    public static void start() {
        initAll();
        startAll();
    }

    public static void initAll() {
        lifeCycles.forEach(LifeCycle::init);
    }

    public static void startAll() {
        lifeCycles.forEach(LifeCycle::start);
    }

    public static void closeAll() {
        lifeCycles.forEach(LifeCycle::close);
    }
}
