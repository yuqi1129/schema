package com.yuqi.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 20/8/20 17:03
 **/
public class ThreadPoolUtil {

    public static final int CPUS = Runtime.getRuntime().availableProcessors() * 2;

    public static final ThreadPoolExecutor FLUSH_POOL;
    public static final ScheduledExecutorService SCHEDULE_POOL;

    static {
        FLUSH_POOL = new ThreadPoolExecutor(
                1,
                CPUS,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100),
                new ThreadFactoryBuilder().setNameFormat("flush_data_%s").build());

        FLUSH_POOL.allowCoreThreadTimeOut(true);
    }

    static {
        SCHEDULE_POOL = new ScheduledThreadPoolExecutor(
                CPUS,
                new ThreadFactoryBuilder().setNameFormat("scedule_flush_data_%s").build());
    }
}
