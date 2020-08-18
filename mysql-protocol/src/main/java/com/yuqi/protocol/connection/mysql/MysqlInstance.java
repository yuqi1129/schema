package com.yuqi.protocol.connection.mysql;

import com.google.common.collect.Sets;
import com.yuqi.sql.SlothTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Set;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 18/8/20 14:44
 **/
public class MysqlInstance {

    public static final Logger LOG = LoggerFactory.getLogger(MysqlInstance.class);

    public static final MysqlInstance INSTANCE = new MysqlInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger(MysqlInstance.class);

    public static final String INSERT_SCHEMA_TEMPLATE = "insert into sloth.schemata values"
            + "('def', '%s', 'utf8', 'utf8_general_ci', NULL)";

    public static boolean isOk = true;

    static {
        try {
            INSTANCE.getConnection();
        } catch (Exception e) {
            LOG.error("init meta database failed, mark meta database is fail, then all db/table you create could not be store");
            isOk = false;
        }
    }

    private String addr;
    private String username;
    private String password;

    private Connection connection = null;

    public MysqlInstance() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            //TODO in configuration
            addr = "jdbc:mysql://localhost:3306";
            username = "root";
            password = "123456";
        } catch (ClassNotFoundException e) {
            LOGGER.error("Can't load mysql driver for:", e);
            throw new RuntimeException(e);
        }
    }


    private Connection getConnection() {
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


    public Set<String> allSchema() {
        final String allSchema = "select schema_name from sloth.schemata";

        Set<String> databases = Sets.newHashSet();
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(allSchema);
            while (resultSet.next()) {
                databases.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (Objects.nonNull(statement)) {
                    statement.close();
                }
            } catch (SQLException e1) {
                LOGGER.error(e1.getMessage());
            }
        }

        return databases;
    }


    public void dropSchema(String schema) {
        final String dropScheamSql = String.format("delete from sloth.schemata where schema_name = '%s'", schema);

        PreparedStatement delete = null;
        Connection connection = getConnection();
        try {
            delete = connection.prepareStatement(dropScheamSql);
            delete.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (Objects.nonNull(delete)) {
                    delete.close();
                }
            } catch (SQLException e1) {
                LOGGER.info(e1.getMessage());
            }
        }
    }

    public void addSchema(String schema) {
        final String schemaExistsSql =
                String.format("select 1 from sloth.schemata where SCHEMA_NAME = '%s'", schema);

        Connection connection = getConnection();
        PreparedStatement query = null;
        Statement insert = null;
        try {
            query = connection.prepareStatement(schemaExistsSql);
            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                LOGGER.warn("schema '{}' already exist, omit .... ", schema);
                return;
            }

            final String insertSchema = String.format(INSERT_SCHEMA_TEMPLATE, schema);
            insert = connection.createStatement();

            insert.execute(insertSchema);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (Objects.nonNull(query)) {
                    query.close();
                }

                if (Objects.nonNull(insert)) {
                    insert.close();
                }
            } catch (Exception e1) {
                //can ignore
                LOGGER.info(e1.getMessage());
            }
        }
    }


    public void addTable(SlothTable slothTable) {
        //todo
    }
}
