package com.yuqi;

import com.google.common.collect.Lists;
import com.yuqi.sql.SlothSchemaHolder;

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
    }

    public static void add(LifeCycle lifeCycle) {
        lifeCycles.add(lifeCycle);
    }

    public static void initAll() {
        lifeCycles.forEach(LifeCycle::init);
    }

    public static void closeAll() {
        lifeCycles.forEach(LifeCycle::close);
    }
}
