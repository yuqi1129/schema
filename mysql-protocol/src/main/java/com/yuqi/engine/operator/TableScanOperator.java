package com.yuqi.engine.operator;

import com.yuqi.engine.io.IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/7/20 15:46
 **/
public class TableScanOperator implements Operator, IO {
    public static final List<Object> EOF = null;

    private ResultSet resultSet;
    private List<String> resultType;
    private Connection connection;

    @Override
    public void open() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3016?useSSL=false", "root", "123456");
            PreparedStatement preparedStatement = connection.prepareStatement("select id, name_cn, dp1, dp2, dp3 from yuqi.employee");

            resultSet = preparedStatement.executeQuery();

            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            final int count = resultSetMetaData.getColumnCount();

            for (int i = 0; i < count; i++) {

            }
        } catch (Exception e) {
        }
    }

    @Override
    public List<Object> next() {
        try {
            if (resultSet.next()) {

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return EOF;
    }

    @Override
    public void close() {
        if (Objects.nonNull(connection)) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
