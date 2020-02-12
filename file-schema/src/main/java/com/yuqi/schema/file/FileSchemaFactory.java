package com.yuqi.schema.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.io.File;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/1/20 15:46
 **/

@Slf4j
public class FileSchemaFactory implements SchemaFactory {
    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        String directory = (String) operand.get("directory");
        String baseDirectory = ((File) operand.get("baseDirectory")).getAbsolutePath();
        return new FileSchema(baseDirectory, directory);
    }
}
