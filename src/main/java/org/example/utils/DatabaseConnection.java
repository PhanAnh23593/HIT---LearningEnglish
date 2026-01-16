package org.example.utils;

import org.example.constant.AppError;
import org.example.constant.AppMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/HIT_Learning_English?useSSL=false&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "Anh2352006@";


    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println(AppError.NOTFOUND_LIBRARY_MYSQL);
            System.err.println(AppMessage.MESSAGE_LIBRARY_MYSQL);
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(AppError.CONNECT_ERROR_MYSQL);
            System.err.println(AppMessage.MESSAGE_CONNECT_MYSQL);
            e.printStackTrace();
        }
        return conn;
    }

}