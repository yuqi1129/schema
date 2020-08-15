package com.yuqi.storage.lucene;

import com.google.common.collect.Lists;
import org.apache.lucene.search.ScoreMode;
import org.apache.lucene.search.SimpleCollector;

import java.io.IOException;
import java.util.List;

import static org.apache.lucene.search.ScoreMode.COMPLETE_NO_SCORES;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 13/8/20 20:19
 **/
public class SlothCollector extends SimpleCollector {

    final List<Integer> docs;

    public SlothCollector() {
        docs = Lists.newArrayList();
    }

    @Override
    public void collect(int doc) throws IOException {
        docs.add(doc);
    }

    @Override
    public ScoreMode scoreMode() {
        return COMPLETE_NO_SCORES;
    }
}
