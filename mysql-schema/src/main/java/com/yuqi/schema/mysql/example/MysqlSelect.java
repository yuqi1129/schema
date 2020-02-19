package com.yuqi.schema.mysql.example;

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
 * @time 14/1/20 17:42
 **/
public class MysqlSelect {
    public static void main(String[] args) {
        try {
            final Properties info = new Properties();
            info.setProperty(META_MODEL,
                    "/Users/yuqi/project/" + "schema/mysql-schema/src/main/resources/mysql.json");

            Connection connection =
                    DriverManager.getConnection("jdbc:calcite:", info);

            Statement statement = connection.createStatement();

            //CodeGen模式下太耗时了....
            //需要引进volcano模型的MPP模式
            for (int i = 0; i < 1; i++) {
                //FIXME 支持中文条件查询
                ResultSet r = statement.executeQuery("select * from yuqi.t1 where varchar_type like '%cn%' limit 10");

                while (r.next()) {
                    System.out.println(r.getString(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
