package com.yuqi.storage.lucene;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.LifeCycle;
import com.yuqi.sql.SlothTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.yuqi.sql.SlothTable.DEFAULT_SHARD;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 13/8/20 17:47
 **/
public class TableEngine implements LifeCycle {

    public static final Logger LOGGER = LoggerFactory.getLogger(TableEngine.class);
    private SlothTable slothTable;

    /**
     * MultiShard instance
     */
    private List<StorageEngine> storageEngines;

    public TableEngine(SlothTable slothTable) {
        this.slothTable = slothTable;
        init();
    }

    public void insert(List<Value> values) {
        int shard = new Random().nextInt(DEFAULT_SHARD);
        final StorageEngine storageEngine = storageEngines.get(shard);

        try {
            storageEngine.insert(values);
        } catch (Exception e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
            storageEngine.setReadOnly(true);
            throw new RuntimeException(e);
        }
    }

    public Iterator<List<Value>> search(QueryContext queryContext) {
        List<Iterator<List<Value>>> searchIts = Lists.newArrayList();

        try {
            for (StorageEngine storageEngine : storageEngines) {
                searchIts.add(storageEngine.query(queryContext));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new SlothMergeIterator<>(searchIts);
    }


    @Override
    public void init() {
        loadData();
    }

    @Override
    public void close() {

    }

    private void loadData() {
        storageEngines = Lists.newArrayList();
        final String basePath = slothTable.buildTableEnginePath();
        for (int shard = 0; shard < DEFAULT_SHARD; shard++) {
            String shardPath = basePath + "/" + shard;
            StorageEngine storageEngine = new LuceneStorageEngine(shardPath, slothTable);
            storageEngine.init();
            storageEngines.add(storageEngine);
        }
    }
}
