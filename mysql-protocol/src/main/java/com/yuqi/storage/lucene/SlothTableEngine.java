package com.yuqi.storage.lucene;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yuqi.LifeCycle;
import com.yuqi.engine.SlothRow;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.SlothTable;
import com.yuqi.sql.util.TypeConversionUtils;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/8/20 17:47
 **/
public class SlothTableEngine implements LifeCycle {

    public static final Logger LOGGER = LoggerFactory.getLogger(SlothTableEngine.class);

    /**
     * Sloth Table instance
     */
    private SlothTable slothTable;

    /**
     * Column name and type, maybe this should change according to variables
     */
    private Map<String, DataType> columnAndDataType = Maps.newHashMap();


    /**
     * All column name
     */
    private List<String> columnNames = Lists.newArrayList();

    /**
     * MultiShard instance
     */
    private List<StorageEngine> storageEngines;


    public SlothTable getSlothTable() {
        return slothTable;
    }

    public List<StorageEngine> getStorageEngines() {
        return storageEngines;
    }

    public SlothTableEngine(SlothTable slothTable) {
        this.slothTable = slothTable;
        init();
    }

    public Map<String, DataType> getColumnAndDataType() {
        return columnAndDataType;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void insert(List<List<Value>> values) {
        int shard = new Random().nextInt(slothTable.getShardNum());
        final StorageEngine storageEngine = storageEngines.get(shard);

        try {
            storageEngine.insert(values);
        } catch (Exception e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
            storageEngine.setReadOnly(true);
            throw new RuntimeException(e);
        }
    }

    public Iterator<SlothRow> search(QueryContext queryContext) {
        List<Iterator<SlothRow>> searchIts = Lists.newArrayList();

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
        resolveType();
        loadData();
    }

    @Override
    public void close() {
        storageEngines.forEach(LifeCycle::close);
    }

    private void loadData() {
        storageEngines = Lists.newArrayList();
        final String basePath = slothTable.buildTableEnginePath();
        for (int shard = 0; shard < slothTable.getShardNum(); shard++) {
            String shardPath = basePath + File.separator + shard;
            StorageEngine storageEngine = new LuceneStorageEngine(shardPath, this);
            storageEngine.init();
            storageEngines.add(storageEngine);
        }
    }

    public void removeData() {
        final String basePath = slothTable.buildTableEnginePath();
        try {
            for (int shard = 0; shard < slothTable.getShardNum(); shard++) {
                String shardPath = basePath + File.separator + shard;
                LOGGER.info("try to delete path '{}'", shardPath);
                FileUtils.deleteDirectory(new File(shardPath));
                LOGGER.info("delete path '{}' succeed", shardPath);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void resolveType() {
        slothTable.getColumns().forEach(column -> {
            final SqlTypeName sqlTypeName = column.getColumnType().getColumnType();
            columnAndDataType.put(column.getColumnName(),
                    TypeConversionUtils.getBySqlTypeName(sqlTypeName));
            columnNames.add(column.getColumnName());
        });
    }
}
