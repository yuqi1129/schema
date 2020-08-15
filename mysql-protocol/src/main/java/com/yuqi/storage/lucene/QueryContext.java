package com.yuqi.storage.lucene;

import org.apache.lucene.search.Query;

import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/8/20 20:31
 **/
public class QueryContext {

    private Query query;
    private Set<String> columnNames;

    public QueryContext(Query query, Set<String> columnNames) {
        this.query = query;
        this.columnNames = columnNames;
    }

    public Query getQuery() {
        return query;
    }

    public Set<String> getColumnNames() {
        return columnNames;
    }
}
