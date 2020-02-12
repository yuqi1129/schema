package com.yuqi.schema.file.csv;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.yuqi.schema.file.AbstractFileReader;
import com.yuqi.schema.file.utils.TypeConvertionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 16:22
 **/
@Getter
@Slf4j
public class CsvFileReader extends AbstractFileReader {
    public CsvFileReader(String dataFilePath, String typeFilePath) {
        super(dataFilePath, typeFilePath);
    }

    @Override
    public Iterator<Object[]> readData() {
        try {

            //try to use csv tools to read data...
            List<String> datas = IOUtils.readLines(new FileInputStream(dataFilePath), Charset.defaultCharset());

            return datas.stream().map(s -> Splitter.on(",").splitToList(s)).map(list -> {
                final int length = list.size();
                final Object[] res = new Object[length];

                for (int i = 0; i < length; i++) {
                    res[i] = TypeConvertionUtils.toObject(fieldTypeEnums.get(i), list.get(i));
                }

                return res;
            }).iterator();
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }

}
