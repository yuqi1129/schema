package com.yuqi.storage.lucene;

import com.google.common.collect.Lists;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.LifeCycle;
import com.yuqi.sql.SlothTable;
import org.apache.lucene.search.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Query;
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
    private List<StorageEngine> list;

    public TableEngine(SlothTable slothTable) {
        this.slothTable = slothTable;
    }

    public void insert(List<Value> values) {
        int shard = new Random().nextInt(DEFAULT_SHARD);
        final StorageEngine storageEngine = list.get(shard);

        try {
            storageEngine.insert(values);
        } catch (Exception e) {
            storageEngine.setReadOnly(true);
        }
    }

    public Collector search(Query query) {
        //todo
        return null;
    }


    @Override
    public void init() {
        loadData();
    }

    @Override
    public void close() {

    }

    private void loadData() {
        list = Lists.newArrayList();
        for (int i = 0; i < DEFAULT_SHARD; i++) {
            StorageEngine storageEngine = new LuceneStorageEngine("", slothTable);
            storageEngine.init();
            list.add(storageEngine);
        }
    }
}
