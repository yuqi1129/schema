package com.yuqi.protocol.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 4/7/20 19:01
 **/
public class T {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3016?useSSL=false", "root", "123456");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "123456");

            Statement statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
