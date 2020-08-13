package com.yuqi.storage.lucene;

import com.google.common.base.Throwables;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import com.yuqi.sql.SlothColumn;
import com.yuqi.sql.SlothTable;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.yuqi.engine.data.type.DataTypes.BYTE;
import static com.yuqi.engine.data.type.DataTypes.DOUBLE;
import static com.yuqi.engine.data.type.DataTypes.FLOAT;
import static com.yuqi.engine.data.type.DataTypes.INTEGER;
import static com.yuqi.engine.data.type.DataTypes.LONG;
import static com.yuqi.engine.data.type.DataTypes.SHORT;
import static com.yuqi.engine.data.type.DataTypes.STRING;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 13/8/20 16:50
 **/
public class LuceneStorageEngine implements StorageEngine {

    public static final Logger LOGGER = LoggerFactory.getLogger(LuceneStorageEngine.class);
    private String storagePath;
    private SlothTable slothTable;

    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private SearcherManager searcherManager;


    private boolean readOnly;


    public LuceneStorageEngine(String storagePath, SlothTable slothTable) {
        this.storagePath = storagePath;
        this.slothTable = slothTable;
    }

    @Override
    public void init() {
        IndexWriterConfig conf = new IndexWriterConfig();
        try {
            indexWriter = new IndexWriter(new NIOFSDirectory(Paths.get(storagePath)), conf);
            DirectoryReader reader = DirectoryReader.open(indexWriter);
            indexReader = new SlothFilterDirectoryReader(reader,
                    new SlothFilterDirectoryReader.SubReaderWrapper(1));

            searcherManager = new SearcherManager(indexWriter, new SearcherFactory());
        } catch (IOException e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insert(List<Value> row) throws IOException {
        final Document document = buildDocument(row);
        indexWriter.addDocument(document);
        return true;
    }

    private Document buildDocument(List<Value> row) {
        final Document document = new Document();
        final List<SlothColumn> columns = slothTable.getColumns();
        for (int i = 0; i < row.size(); i++) {
            final Value value = row.get(i);
            final DataType dataType = value.getType();

            if (BYTE.equals(dataType) || SHORT.equals(dataType) || INTEGER.equals(dataType)) {
                document.add(new IntPoint(columns.get(i).getColumnName(), value.intValue()));
                document.add(new StoredField(columns.get(i).getColumnName(), value.intValue()));
            } else if (LONG.equals(dataType)) {
                document.add(new LongPoint(columns.get(i).getColumnName(), value.longValue()));
                document.add(new StoredField(columns.get(i).getColumnName(), value.longValue()));
            } else if (FLOAT.equals(dataType)) {
                document.add(new LongPoint(columns.get(i).getColumnName(), value.longValue()));
                document.add(new StoredField(columns.get(i).getColumnName(), value.floatValue()));
            } else if (DOUBLE.equals(dataType)) {
                document.add(new DoublePoint(columns.get(i).getColumnName(), value.doubleValue()));
                document.add(new StoredField(columns.get(i).getColumnName(), value.doubleValue()));
            } else if (STRING.equals(dataType)) {
                document.add(new StringField(columns.get(i).getColumnName(), value.stringValue(), Field.Store.YES));
                document.add(new StoredField(columns.get(i).getColumnName(), value.stringValue()));
            } else {
                //maybe time/datetime/timestamp
                document.add(new LongPoint(columns.get(i).getColumnName(), value.longValue()));
                document.add(new StoredField(columns.get(i).getColumnName(), value.floatValue()));
            }
        }

        return document;
    }

    @Override
    public void query(Query query, Collector collector) throws IOException {
        final IndexSearcher searcher = searcherManager.acquire();
        searcher.search(query, collector);
    }

    @Override
    public void close() {
        try {
            indexWriter.close();
            indexReader.close();
        } catch (IOException e) {
            //ingore exception
            LOGGER.error(Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public boolean readOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
