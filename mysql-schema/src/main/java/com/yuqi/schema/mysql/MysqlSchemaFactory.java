package com.yuqi.schema.mysql;


import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

import static com.yuqi.schema.common.constants.CommonConstant.PASSWORD;
import static com.yuqi.schema.common.constants.CommonConstant.SCHEMA;
import static com.yuqi.schema.common.constants.CommonConstant.URL;
import static com.yuqi.schema.common.constants.CommonConstant.USER_NAME;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:10
 **/
public class MysqlSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        String url = (String) operand.get(URL);
        String username = (String) operand.get(USER_NAME);
        String password = (String) operand.get(PASSWORD);
        String schema = (String) operand.get(SCHEMA);
        return new MysqlSchema(url, username, password, schema);
    }
}
