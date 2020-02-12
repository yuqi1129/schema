package com.yuqi.schema.file.csv;

import com.yuqi.schema.file.AbstractFileReader;
import com.yuqi.schema.file.FileTable;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 16:17
 **/
public class CsvFileTable extends FileTable {
    public CsvFileTable(AbstractFileReader fileReader) {
        super(fileReader);
    }
}
