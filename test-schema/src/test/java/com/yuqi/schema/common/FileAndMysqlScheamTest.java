package com.yuqi.schema.common;

import org.apache.calcite.avatica.InternalProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static com.yuqi.schema.common.constants.CommonConstant.CALCITE_URL;
import static com.yuqi.schema.common.constants.MetaConstants.META_MODEL;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 8/2/20 17:36
 **/
public class FileAndMysqlScheamTest {
    public static void main(String[] args) {
        try {
            final Statement statement = getStatement();
            final String query1 = "select tab1.bigint_type a, tab1.char_type b, tab2.varchar_type c from csv.t1 tab1 inner join yuqi.type tab2 on "
                    + " tab1.int_type = tab2.int_type"
                    + " where tab1.smallint_type < 4";
            final String query = "select smallint_type + 1, int_type from csv.t1 where tinyint_type is null";

            for (int i = 0; i < 1; i++) {
                //FIXME join条件是integer = integer 没问题，但 long = long 就会有问题
                ResultSet r = statement.executeQuery(query1);

                while (r.next()) {
                    System.out.println(r.getInt(1) + ", " + r.getString(2) + ", " + r.getString(3));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Statement getStatement() throws SQLException {
        final Properties info = new Properties();
        info.setProperty(META_MODEL,
                "/Users/yuqi/project/" + "schema/test-schema/src/test/resources/schema.json");

        info.setProperty(InternalProperty.CASE_SENSITIVE.name(), "false");
        Connection connection =
                DriverManager.getConnection(CALCITE_URL, info);

        return connection.createStatement();
    }
}
