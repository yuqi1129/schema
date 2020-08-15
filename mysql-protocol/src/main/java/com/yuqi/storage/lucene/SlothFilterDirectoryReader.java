package com.yuqi.storage.lucene;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FilterDirectoryReader;
import org.apache.lucene.index.LeafReader;

import java.io.IOException;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 12/8/20 21:17
 **/
public class SlothFilterDirectoryReader extends FilterDirectoryReader {

    private final FilterDirectoryReader.SubReaderWrapper wrapper;

    public SlothFilterDirectoryReader(DirectoryReader in, SubReaderWrapper wrapper) throws IOException {
        super(in, wrapper);
        this.wrapper = wrapper;
    }

    @Override
    protected DirectoryReader doWrapDirectoryReader(DirectoryReader in) throws IOException {
        return new SlothFilterDirectoryReader(in, (SubReaderWrapper) wrapper);

    }

    @Override
    public CacheHelper getReaderCacheHelper() {
        return in.getReaderCacheHelper();
    }

    static final class SubReaderWrapper extends FilterDirectoryReader.SubReaderWrapper {

        private final int shardId;

        SubReaderWrapper(int shardId) {
            this.shardId = shardId;
        }

        @Override
        public LeafReader wrap(LeafReader reader) {
            return new ElasticsearchLeafReader(reader, shardId);
        }
    }
}
