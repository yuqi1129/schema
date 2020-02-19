package com.yuqi.schema.mysql;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:14
 **/

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Slf4j
public class MysqlSchema extends AbstractSchema {
    private final String url;
    private final String username;
    private final String password;
    private final String schema;

    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private Map<String, Table> tableMap;

    @Override
    protected Map<String, Table> getTableMap() {
        //ignore;
        if (null != tableMap && tableMap.size() > 0) {
            return tableMap;
        }

        Connection connection;
        try {
            Class.forName(DRIVER);
            //should need
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select table_name from information_schema.tables where table_type = ? and TABLE_SCHEMA = ?");

            preparedStatement.setString(1, "BASE TABLE");
            preparedStatement.setString(2, schema);

            ResultSet resultSet = preparedStatement.executeQuery();

            tableMap = Maps.newHashMap();
            while (resultSet.next()) {
                String tableName = resultSet.getString(1).toUpperCase();
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
