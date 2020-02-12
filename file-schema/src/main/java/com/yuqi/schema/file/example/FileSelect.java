package com.yuqi.schema.file.example;

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
public class FileSelect {
    public static void main(String[] args) {
        try {
            final Properties info = new Properties();
            info.setProperty(META_MODEL,
                    "/Users/yuqi/project/" + "schema/file-schema/src/main/resources/file.json");
            Connection connection =
                    DriverManager.getConnection("jdbc:calcite:", info);

            Statement statement = connection.createStatement();


            //dag;

            //CodeGen模式下太耗时了....
            //需要引进volcano模型的MPP模式
            for (int i = 0; i < 1; i++) {
                ResultSet r = statement.executeQuery("select TABLE1.ID, TABLE1.NAME, TABLE2.ADDR from CSV.T1 TABLE1 "
                        + "INNER JOIN JSON.T2 TABLE2 on TABLE1.ID = TABLE2.ID where TABLE1.ID + 1 > 2");

                while (r.next()) {
                    System.out.println(r.getInt(1) + ", " + r.getString(2) + ", " + r.getString(3));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
