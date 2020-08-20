package com.yuqi.storage.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import java.nio.file.Paths;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 11/8/20 22:29
 **/
public class Test {
    public static void main(String[] args) {
        Directory d;
        IndexWriterConfig conf = new IndexWriterConfig();

//        List<IndexableField> list = Lists.newArrayList();

        //基础索引存储用Lucene
        //额外加一些东西，如min/max/sum/count/null size等统计数据
        //比如说select count(*) 这里直接将agg 推到存储层, 在上层做比较
//        list.add(new IntPoint("age", 75));
//        list.add(new TextField("name", "Hello world", Field.Store.YES));

        try {

            final IndexWriter indexWriter =
                    new IndexWriter(new NIOFSDirectory(Paths.get("/tmp/test/db1/person/0")), conf);

            //DirectoryReader reader = DirectoryReader.open(indexWriter);
            DirectoryReader reader = DirectoryReader.open(new NIOFSDirectory(Paths.get("/tmp/test/db1/person/0")));
            final IndexReader indexReader =
                    new SlothFilterDirectoryReader(reader, new SlothFilterDirectoryReader.SubReaderWrapper(1));
            SearcherManager searcherManager = new SearcherManager(reader, new SearcherFactory());

            Document document = new Document();

            document.add(new IntPoint("age", 60));
            document.add(new StoredField("age", 60));

            document.add(new FloatPoint("score", 95.5f));
            document.add(new StoredField("score", 95.5f));

            //document.add(new TextField("title", "English course", Field.Store.YES));
            //document.add(new TextField("name", "Good enough", Field.Store.YES));
            //indexWriter.addDocument(document);

            IndexSearcher searcher = new IndexSearcher(indexReader);

            //IndexSearcher searcher = searcherManager.acquire();

            //TopDocs topDocs = searcher.search(new TermQuery(new Term("title", "course")),  10);
            //TopDocs topDocs = searcher.search(FloatPoint.newRangeQuery("score", 60, Float.POSITIVE_INFINITY), 10);
            //Document r = searcher.doc(topDocs.scoreDocs[0].doc);
            //List<IndexableField> fields = r.getFields();

            SlothCollector slothCollector = new SlothCollector();
            searcher.search(new MatchAllDocsQuery(), slothCollector);


//            indexWriter.flush();
//            indexWriter.maybeMerge();

            System.out.println("xxx");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
