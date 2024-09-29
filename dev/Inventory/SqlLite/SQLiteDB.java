package dev.Inventory.SqlLite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDB {

    private static final String URL = "jdbc:sqlite:inventory.db"; // Ensure the file path is correct

    // Unified method to establish a connection to the database
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        if (conn != null) {
            System.out.println("Connection to SQLite has been established.");
        }
        return conn;
    }
}

