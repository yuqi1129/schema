package com.yuqi.protocol.connection.mysql;

import com.google.common.collect.Sets;
import com.yuqi.protocol.meta.tables.pojos.Schemata;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yuqi.protocol.meta.Sloth.SLOTH;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/8/20 10:12
 **/
public class SchemaMeta {
    public static final Logger LOGGER = LoggerFactory.getLogger(SchemaMeta.class);
    public static final SchemaMeta INSTANCE = new SchemaMeta(MysqlConnection.INSTANCE);

    private MysqlConnection mysqlConnection;

    public SchemaMeta(MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
    }

    public boolean schemaIsOk() {
        return mysqlConnection.isOk();
    }

    public Set<String> allSchema() {
         if (!mysqlConnection.isOk()) {
             return Sets.newHashSet();
         }

         final DSLContext dslContext = mysqlConnection.getDslContext();
         List<Schemata> schemataList = dslContext.selectFrom(SLOTH.SCHEMATA)
                 .fetchInto(Schemata.class);

         return schemataList.stream().map(Schemata::getSchemaName).collect(Collectors.toSet());
    }


    public void dropSchema(String schema) {
        if (!mysqlConnection.isOk()) {
            return;
        }

        final DSLContext dslContext = mysqlConnection.getDslContext();
        final int r = dslContext.deleteFrom(SLOTH.SCHEMATA)
                .where(SLOTH.SCHEMATA.SCHEMA_NAME.eq(schema))
                .execute();

        if (r == 0) {
            LOGGER.warn("schema '{}' does not exist, pay attention...", schema);
        }
    }

    public void addSchema(String schema) {
        if (!mysqlConnection.isOk()) {
            return;
        }

        //TODO remove dsl and use mybatis, or you can use spring to implement this
        final DSLContext dslContext = mysqlConnection.getDslContext();
        final int count = dslContext.selectCount().from(SLOTH.SCHEMATA)
                .where(SLOTH.SCHEMATA.SCHEMA_NAME.eq(schema))
                .fetchOne()
                .value1();


        if (count > 0) {
            LOGGER.warn("schema '{}' already exist, omit .... ", schema);
            return;
        }

        dslContext.insertInto(SLOTH.SCHEMATA)
                .values("def", schema, "utf8", "utf8_general_ci", null)
                .execute();
    }
}
