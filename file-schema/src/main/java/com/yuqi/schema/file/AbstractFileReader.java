package com.yuqi.schema.file;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.schema.common.util.FieldTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 16:19
 **/
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractFileReader {
    protected final String dataFilePath;
    protected final String typeFilePath;

    protected RelDataType rowType;
    protected List<FieldTypeEnum> fieldTypeEnums = Lists.newArrayList();

    public abstract Iterator<Object[]> readData();


    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        if (null != rowType) {
            return rowType;
        }

        try {
            List<String> metaContents = IOUtils.readLines(
                    new FileInputStream(typeFilePath), Charset.defaultCharset());

            List<String> columnsNames = Lists.newArrayList();
            List<RelDataType> columnTypes = Lists.newArrayList();
            metaContents.forEach(s -> {
                String[] nameAndType = s.split(":");
                if (nameAndType.length != 2) {
                    throw new IllegalArgumentException(String.format("name and type string '%s' is invalid....", s));
                }
                columnsNames.add(nameAndType[0].toUpperCase());
                final Class c = FieldTypeEnum.getByTypeName(nameAndType[1]);
                columnTypes.add(typeFactory.createJavaType(c));

                fieldTypeEnums.add(FieldTypeEnum.getByType(nameAndType[1]));
            });


            rowType = typeFactory.createStructType(columnTypes, columnsNames);
            return rowType;
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e);
        }
    }
}
