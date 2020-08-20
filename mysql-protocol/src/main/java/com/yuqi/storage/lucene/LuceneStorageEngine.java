package com.yuqi.storage.lucene;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.engine.data.type.DataType;
import com.yuqi.engine.data.value.Value;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatPoint;
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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yuqi.engine.data.type.DataTypes.BYTE;
import static com.yuqi.engine.data.type.DataTypes.DOUBLE;
import static com.yuqi.engine.data.type.DataTypes.FLOAT;
import static com.yuqi.engine.data.type.DataTypes.INTEGER;
import static com.yuqi.engine.data.type.DataTypes.LONG;
import static com.yuqi.engine.data.type.DataTypes.SHORT;
import static com.yuqi.engine.data.type.DataTypes.STRING;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
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

    private volatile int dataNumUncommited = 0;
    private volatile long lastFlushTime = System.currentTimeMillis();

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

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {

                    //TODO only this can generate segment file, or when restart
                    // data will lost
                    indexWriter.commit();
                } catch (Exception e) {
                    //ignore
                    LOGGER.error(e.getMessage());
                }
            }));
        } catch (IOException e) {
            LOGGER.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insert(List<List<Value>> rows) throws IOException {

        if (readOnly) {
            LOGGER.error("Storage engine is mark read only, can't flush");
            return false;
        }

        // async, 5 seconds later, data can be query
        for (List<Value> row : rows) {
            final Document document = rowToDocument(row);
            indexWriter.addDocument(document);
        }

        dataNumUncommited = dataNumUncommited + rows.size();
        return true;
    }

    private void updateIndexWriterAndReader() throws IOException {
        indexWriter.flush();

        DirectoryReader reader = DirectoryReader.open(indexWriter);
        indexReader = new SlothFilterDirectoryReader(reader,
                new SlothFilterDirectoryReader.SubReaderWrapper(1));


        searcherManager = new SearcherManager(indexWriter, new SearcherFactory());
    }

    private Document rowToDocument(List<Value> row) {
        final Document document = new Document();

        final List<String> columnNames = tableEngine.getColumnNames();
        final Map<String, DataType> dataTypeList = tableEngine.getColumnAndDataType();

        //TODO 目测lucene 当前无法存NULL值，NULL值需要自已处理
        // NULL 映射某个固定的值

        /**
         * Store 'NULL' to String null
         *
         * for number type, minimum number of the type represent null value
         */
        for (int i = 0; i < row.size(); i++) {
            final Value value = row.get(i);
            final String columnName = columnNames.get(i);
            final DataType dataType = dataTypeList.get(columnName);

            if (BYTE.equals(dataType) || SHORT.equals(dataType) || INTEGER.equals(dataType)) {
                int content = value.isNull() ? Integer.MIN_VALUE : value.intValue();
                document.add(new IntPoint(columnName, content));
                document.add(new StoredField(columnName, content));
            } else if (LONG.equals(dataType)) {
                long content = value.isNull() ? Long.MIN_VALUE : value.longValue();
                document.add(new LongPoint(columnName, content));
                document.add(new StoredField(columnName, content));
            } else if (FLOAT.equals(dataType)) {
                float content = value.isNull() ? Float.MIN_VALUE : value.floatValue();
                document.add(new FloatPoint(columnName, content));
                document.add(new StoredField(columnName, content));
            } else if (DOUBLE.equals(dataType)) {
                double content = value.isNull() ? Double.MIN_VALUE : value.doubleValue();
                document.add(new DoublePoint(columnName, content));
                document.add(new StoredField(columnName, content));
            } else if (STRING.equals(dataType)) {
                //String values do not need to add store field
                String content = value.isNull() ? "NULL" : value.stringValue();
                document.add(new StringField(columnName, content, Field.Store.YES));
            } else {
                //maybe time/datetime/timestamp
                long content = value.isNull() ? Long.MIN_VALUE : value.longValue();
                document.add(new LongPoint(columnName, content));
                document.add(new StoredField(columnName, content));
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

            Value v;
            if (dataType == STRING) {
                final String stringValue = field.stringValue();
                v = new Value(Objects.equals("NULL", stringValue) ? null : stringValue, dataType);
            } else if (dataType == INTEGER || dataType == SHORT || dataType == BYTE) {
                Integer numericValue = (Integer) field.numericValue();
                v = new Value(Integer.MIN_VALUE == numericValue ? null : numericValue, dataType);
            } else if (dataType == LONG) {
                Long numericValue = (Long) field.numericValue();
                v = new Value(Long.MIN_VALUE == numericValue ? null : numericValue, dataType);
            } else if (dataType == FLOAT) {
                Float numericValue = (Float) field.numericValue();
                v = new Value(Float.MIN_VALUE == numericValue ? null : numericValue, dataType);
            } else if (dataType == DOUBLE) {
                Double numericValue = (Double) field.numericValue();
                v = new Value(Double.MIN_VALUE == numericValue ? null : numericValue, dataType);
            } else {
                Long numericValue = (Long) field.numericValue();
                v = new Value(Long.MIN_VALUE == numericValue ? null : numericValue, dataType);
            }
            rs.add(v);

        }

        return rs;
    }

    @Override
    public Iterator<List<Value>> query(QueryContext queryContext) throws IOException {

        final IndexSearcher searcher = new IndexSearcher(indexReader);
        //final IndexSearcher searcher = searcherManager.acquire();
        SlothCollector collector = new SlothCollector();

        //TODO, MAX_VALLUE will show down the query
        TopDocs topDocs = searcher.search(queryContext.getQuery(), Integer.MAX_VALUE);


        //这里太丑陋了, 需要好好优化一下
        return Arrays.stream(topDocs.scoreDocs).parallel()
                .map(scoreDoc -> {
                    try {
                        return indexReader.document(scoreDoc.doc, queryContext.getColumnNames());
                    } catch (IOException e) {
                        LOGGER.error("get doc '{}' meets error:", scoreDoc.doc, e);
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

    @Override
    public void flush() {
        //todo make config

        if (readOnly) {
            LOGGER.error("Storage engine is mark read only, can't flush");
            return;
        }

        LOGGER.info("Start to flush, current thread = {}", Thread.currentThread());
        if (dataNumUncommited > 0) {
            try {
                updateIndexWriterAndReader();
                dataNumUncommited = 0;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean shouldFlush() {
        //at last after 1s we should flush data
        return dataNumUncommited > 0 || System.currentTimeMillis() - lastFlushTime > 1000;
    }
}
