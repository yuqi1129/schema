package com.yuqi.schema.mysql;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import static com.yuqi.schema.common.constants.CommonConstant.MYSQL_DRIVER;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:14
 **/

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class MysqlSchema extends AbstractSchema {
    public static final Logger log = LoggerFactory.getLogger(MysqlSchema.class);
    private final String url;
    private final String username;
    private final String password;
    private final String schema;

    private static final String MYSQL_GET_SCHEMA_SENTENCE = "select table_name from information_schema.tables where table_type = ? and TABLE_SCHEMA = ?";
    private static final String NORMAL_SCHEMA_TYPE = "BASE TABLE";

    private Map<String, Table> tableMap;

    @Override
    protected Map<String, Table> getTableMap() {
        if (MapUtils.isNotEmpty(tableMap)) {
            return tableMap;
        }

        Connection connection;
        try {
            Class.forName(MYSQL_DRIVER);
            //should need
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(MYSQL_GET_SCHEMA_SENTENCE);

            preparedStatement.setString(1, NORMAL_SCHEMA_TYPE);
            preparedStatement.setString(2, schema);

            ResultSet resultSet = preparedStatement.executeQuery();

            tableMap = Maps.newHashMap();
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                Table mysqlTable = new MysqlTable(schema, tableName, connection);
                tableMap.put(tableName, mysqlTable);
            }

            return tableMap;
        } catch (Exception e1) {
            log.error("Close connection error:" + e1);
            throw new RuntimeException(e1);
        }
    }
}
