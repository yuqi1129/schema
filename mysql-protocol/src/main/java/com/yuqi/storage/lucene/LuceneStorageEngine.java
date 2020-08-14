package com.yuqi.storage.lucene;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
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
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private TableEngine tableEngine;

    private IndexWriter indexWriter;
    private IndexReader indexReader;
    private SearcherManager searcherManager;

    private boolean readOnly;

    public LuceneStorageEngine(String storagePath, TableEngine tableEngine) {
        this.storagePath = storagePath;
        this.tableEngine = tableEngine;
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
        final Document document = rowToDocument(row);
        indexWriter.addDocument(document);
        return true;
    }

    private Document rowToDocument(List<Value> row) {
        final Document document = new Document();

        final List<String> columnNames = tableEngine.getColumnNames();
        final Map<String, DataType> dataTypeList = tableEngine.getColumnAndDataType();

        for (int i = 0; i < row.size(); i++) {
            final Value value = row.get(i);
            final String columnName = columnNames.get(i);
            final DataType dataType = dataTypeList.get(columnName);

            if (BYTE.equals(dataType) || SHORT.equals(dataType) || INTEGER.equals(dataType)) {
                document.add(new IntPoint(columnName, value.intValue()));
                document.add(new StoredField(columnName, value.intValue()));
            } else if (LONG.equals(dataType)) {
                document.add(new LongPoint(columnName, value.longValue()));
                document.add(new StoredField(columnName, value.longValue()));
            } else if (FLOAT.equals(dataType)) {
                document.add(new LongPoint(columnName, value.longValue()));
                document.add(new StoredField(columnName, value.floatValue()));
            } else if (DOUBLE.equals(dataType)) {
                document.add(new DoublePoint(columnName, value.doubleValue()));
                document.add(new StoredField(columnName, value.doubleValue()));
            } else if (STRING.equals(dataType)) {
                document.add(new StringField(columnName, value.stringValue(), Field.Store.YES));
                //should add to store value;
                document.add(new StoredField(columnName, value.stringValue()));
            } else {
                //maybe time/datetime/timestamp
                document.add(new LongPoint(columnName, value.longValue()));
                document.add(new StoredField(columnName, value.floatValue()));
            }
        }

        return document;
    }

    private List<Value> documentToRow(Document document) {

        final Map<String, DataType> columnAndDataType = tableEngine.getColumnAndDataType();
        //TODO 可能只select部分列，目前这里是选择全部的列，效率不太好
        List<Value> rs = Lists.newArrayList();
        final List<IndexableField> fields = document.getFields();
        for (int i = 0; i < columnAndDataType.size(); i++) {
            IndexableField field = fields.get(i);

            String columneName = field.name();
            DataType dataType = columnAndDataType.get(columneName);

            if (dataType == STRING) {
                rs.add(new Value(field.stringValue(), dataType));
            } else {
                rs.add(new Value(field.numericValue(), dataType));
            }
        }

        return rs;
    }

    @Override
    public Iterator<List<Value>> query(QueryContext queryContext) throws IOException {
        final IndexSearcher searcher = searcherManager.acquire();
        SlothCollector collector = new SlothCollector();

        searcher.search(queryContext.getQuery(), collector);

        //这里太丑陋了, 需要好好优化一下
        return collector.docs.parallelStream()
                .map(docId -> {
                    try {
                        return indexReader.document(docId, queryContext.getColumnNames());
                    } catch (IOException e) {
                        LOGGER.error("get doc '{}' meets error:", docId, e);
                        throw new RuntimeException(e);
                    }
                })
                .map(this::documentToRow)
                .iterator();
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
