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
 * @time 10/8/20 17:27
 **/
public abstract class AbstractStorageEngine {

    protected Table table;

    protected List<AbstractColumn> columns;

    protected String storePath;


    public AbstractStorageEngine(Table table, List<AbstractColumn> columns, String storePath) {
        this.table = table;
        this.columns = columns;
        this.storePath = storePath;
    }

    public abstract boolean insert(List<List<Value>> rows);

    //todo, 暂时不知道query condition,
    public abstract Iterator<List<Value>> query();


}
