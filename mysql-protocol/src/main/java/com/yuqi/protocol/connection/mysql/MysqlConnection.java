package com.yuqi.protocol.connection.mysql;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.util.postgres.PostgresDSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 18/8/20 14:44
 **/
public class MysqlConnection {
    public static final Logger LOG = LoggerFactory.getLogger(MysqlConnection.class);

    public static final MysqlConnection INSTANCE = new MysqlConnection();
    public static final Logger LOGGER = LoggerFactory.getLogger(MysqlConnection.class);

    private boolean isOk = true;

    private String addr;
    private String username;
    private String password;

    private Connection connection = null;
    private DSLContext dslContext;

    public MysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            //TODO make configuration
            addr = "jdbc:mysql://localhost:3306";
            username = "root";
            password = "123456";

            getConnection();

            dslContext = PostgresDSL.using(connection, SQLDialect.MYSQL);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Can't load mysql driver for:", e);
            throw new RuntimeException(e);
        } catch (RuntimeException e1) {
            LOG.error("init meta database failed, mark meta database is fail, then all db/table you create could not be store");
            isOk = false;
        }
    }


    public Connection getConnection() {
        try {
            if (Objects.nonNull(connection) && !connection.isClosed()) {
                return connection;
            }
            connection = DriverManager.getConnection(addr, username, password);
            return connection;
        } catch (SQLException e) {
            LOGGER.info("can't get connection:", e);
            throw new RuntimeException(e);
        }
    }

    public boolean isOk() {
        return isOk;
    }

    public DSLContext getDslContext() {
        return dslContext;
    }
}
