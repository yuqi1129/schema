package com.yuqi.schema.common;

import org.apache.calcite.avatica.InternalProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

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
            final Properties info = new Properties();
            info.setProperty(META_MODEL,
                    "/Users/yuqi/project/" + "schema/test-schema/src/main/resources/file_and_mysql.json");

            info.setProperty(InternalProperty.CASE_SENSITIVE.name(), "false");
            Connection connection =
                    DriverManager.getConnection("jdbc:calcite:", info);

            Statement statement = connection.createStatement();


            //CodeGen模式下太耗时了....
            //需要引进volcano模型的MPP模式
            for (int i = 0; i < 1; i++) {
                //FIXME join条件是integer = integer 没问题，但 long = long 就会有问题
                ResultSet r = statement.executeQuery("select TABLE1.ID, TABLE1.NAME, TABLE2.NAME from csv.T1 TABLE1 "
                        + "INNER JOIN YUQI.TEST TABLE2 on TABLE1.ID = TABLE2.ID");

                while (r.next()) {
                    System.out.println(r.getInt(1) + ", " + r.getString(2) + ", " + r.getString(3));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
