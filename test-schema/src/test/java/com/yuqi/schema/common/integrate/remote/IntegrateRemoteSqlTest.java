package com.yuqi.schema.common.integrate.remote;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
public abstract class IntegrateRemoteSqlTest extends IntegrateRemoteTestBase {

    private final String connectionConf;
    private Connection connection;

    public IntegrateRemoteSqlTest(String filePath, String connectionConf) {
        super(filePath);
        this.connectionConf = connectionConf;
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

    /**
     * Default implementation is mysql
     * @return
     */
    @Override
    public Statement getStatement() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(connectionConf)) {
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
