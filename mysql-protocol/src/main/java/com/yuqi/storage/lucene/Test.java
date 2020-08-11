package com.yuqi.storage.lucene;

import com.google.common.collect.Lists;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import java.nio.file.Paths;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 11/8/20 22:29
 **/
public class Test {
    public static void main(String[] args) {
        Directory d;
        IndexWriterConfig conf = new IndexWriterConfig();

        List<IndexableField> list = Lists.newArrayList();

        //基础索引存储用Lucene
        //额外加一些东西，如min/max/sum/count/null size等统计数据
        //比如说select count(*) 这里直接将agg 推到存储层, 在上层做比较
        list.add(new IntPoint("age", 75));
        list.add(new TextField("name", "Hello world", Field.Store.YES));

        try {

            final IndexWriter indexWriter =
                    new IndexWriter(new NIOFSDirectory(Paths.get("/tmp/test")), conf);

            indexWriter.addDocument(list);

            indexWriter.flush();
            indexWriter.maybeMerge();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
