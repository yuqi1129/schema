package com.yuqi.storage.engine;

import com.yuqi.engine.data.value.Value;
import com.yuqi.storage.column.AbstractColumn;
import org.apache.calcite.schema.Table;

import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 5/7/20 16:55
 **/
public class BlockEngine extends AbstractStorageEngine {

    public BlockEngine(Table table, List<AbstractColumn> columns, String storePath) {
        super(table, columns, storePath);
    }

    @Override
    public boolean insert(List<List<Value>> rows) {
        //block engine do nt support insert
        return false;
    }

    @Override
    public Iterator<List<Value>> query() {
        return null;
    }
}
