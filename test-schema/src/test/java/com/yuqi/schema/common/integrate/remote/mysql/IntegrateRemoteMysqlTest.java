package com.yuqi.schema.common.integrate.remote.mysql;

import com.fasterxml.jackson.databind.JsonNode;
import com.yuqi.schema.common.integrate.remote.IntegrateRemoteTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static com.yuqi.schema.common.constants.CommonConstant.MYSQL_DRIVER;
import static com.yuqi.schema.common.constants.CommonConstant.OBJECT_MAPPER;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 17/2/20 20:12
 **/
@Slf4j
public class IntegrateRemoteMysqlTest extends IntegrateRemoteTestBase {

    private final String mysqlConf;
    private Connection connection;

    public IntegrateRemoteMysqlTest(String filePath, String mysqlConf) {
        super(filePath);
        this.mysqlConf = mysqlConf;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/remote/mysql/select/select1.sql", "sql_and_result/remote/mysql/mysql_config.json" }
        });
    }

    @Override
    public void after() {
        super.after();
        try {
            if (Objects.nonNull(connection) && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error(e.toString());
        }
    }

    @Override
    public Statement getStatement() {

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(mysqlConf)) {
            final JsonNode node = OBJECT_MAPPER.readTree(inputStream);

            final String url = node.get("url").textValue();
            final String username = node.get("username").textValue();
            final String password = node.get("password").textValue();
            final String defaultSchema = node.get("schema").textValue();

            Class.forName(MYSQL_DRIVER);
            connection = DriverManager.getConnection(url, username, password);
            connection.setSchema(defaultSchema);
            return connection.createStatement();
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

}
