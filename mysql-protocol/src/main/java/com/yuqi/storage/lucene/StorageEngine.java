package com.yuqi.storage.lucene;

import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.LifeCycle;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;

import java.io.IOException;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 13/8/20 16:58
 **/
public interface StorageEngine extends LifeCycle {

    /**
     * Insert row
     *
     * @param row
     * @return
     */
    boolean insert(List<Value> row) throws IOException;

    /**
     * Query
     *
     * @param query
     * @param collector
     */
    void query(Query query, Collector collector) throws IOException;


    /**
     * Mark read only
     *
     * @return
     */
    default boolean readOnly() {
        return false;
    }


    /**
     * @param readOnly
     */
    default void setReadOnly(boolean readOnly) {
        //todo
    }
}
