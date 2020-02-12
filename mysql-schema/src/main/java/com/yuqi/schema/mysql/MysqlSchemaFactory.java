package com.yuqi.schema.mysql;


import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:10
 **/
public class MysqlSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        String url = (String) operand.get("url");
        String username = (String) operand.get("username");
        String password = (String) operand.get("password");
        String schema = (String) operand.get("schema");
        return new MysqlSchema(url, username, password, schema);
    }
}
