package com.yuqi.storage.lucene;

import org.apache.lucene.index.FilterLeafReader;
import org.apache.lucene.index.LeafReader;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 12/8/20 21:25
 **/
public class ElasticsearchLeafReader extends FilterLeafReader {
    private final int shardId;

    public ElasticsearchLeafReader(LeafReader in, int shardId) {
        super(in);
        this.shardId = shardId;
    }

    @Override
    public CacheHelper getCoreCacheHelper() {
        return in.getCoreCacheHelper();
    }

    @Override
    public CacheHelper getReaderCacheHelper() {
        return in.getReaderCacheHelper();
    }
}
