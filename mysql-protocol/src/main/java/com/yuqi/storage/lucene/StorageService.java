package com.yuqi.storage.lucene;

import com.yuqi.LifeCycle;
import com.yuqi.sql.SlothSchemaHolder;
import com.yuqi.sql.SlothTable;
import com.yuqi.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yuqi.util.ThreadPoolUtil.FLUSH_POOL;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 20/8/20 17:14
 **/
public class StorageService implements LifeCycle {
    public static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    public static final StorageService INSTANCE = new StorageService();

    @Override
    public void init() {
        scheduleFlushData();
    }

    public void scheduleFlushData() {
        LOGGER.info("start peroid thread to check storage engine");
        ThreadPoolUtil.SCHEDULE_POOL.scheduleAtFixedRate(() -> {
            final List<StorageEngine> storageEngines = getShouldFlush();
            storageEngines.forEach(storageEngine -> FLUSH_POOL.submit(storageEngine::flush));
        }, 5, 5, TimeUnit.SECONDS);
    }


    private List<StorageEngine> getShouldFlush() {
        LOGGER.info("Start to check if any table needs to flush data...");
        return SlothSchemaHolder.INSTANCE.getSchemaMap().stream()
                .flatMap(schema -> schema.getAllTable().stream())
                .map(table -> ((SlothTable) table).getSlothTableEngine())
                .flatMap(tableEngine -> tableEngine.getStorageEngines().stream())
                .filter(StorageEngine::shouldFlush)
                .collect(Collectors.toList());
    }

    @Override
    public void close() {
       //do nothing
    }
}
