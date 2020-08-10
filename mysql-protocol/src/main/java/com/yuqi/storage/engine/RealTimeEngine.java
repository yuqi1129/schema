package com.yuqi.storage.engine;

import com.yuqi.engine.data.value.Value;
import com.yuqi.storage.column.AbstractColumn;
import org.apache.calcite.schema.Table;

import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 16:55
 **/
public class RealTimeEngine extends AbstractStorageEngine {

    /**
     * @param table
     * @param columns
     * @param storePath, for realtime engine, storage path is checkpoint path
     */
    public RealTimeEngine(Table table, List<AbstractColumn> columns, String storePath) {
        super(table, columns, storePath);
    }

    @Override
    public boolean insert(List<List<Value>> rows) {
        rows.forEach(row -> {
            for (int i = 0; i < columns.size(); i++) {
                Value value = row.get(i);
                AbstractColumn abstractColumn = columns.get(i);
                abstractColumn.put(value.getValueByType());
            }
        });

        return true;

    }

    @Override
    public Iterator<List<Value>> query() {
        return null;
    }
}
