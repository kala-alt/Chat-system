package com.example.Chat_system.Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLJDBC {
    
        private static final String url = "jdbc:postgresql://localhost:5432/Chat System";
        private static final String user = "postgres";
        private static final String password = "1921";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

}
