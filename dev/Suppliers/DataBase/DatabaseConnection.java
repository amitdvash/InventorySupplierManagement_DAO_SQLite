package dev.Suppliers.DataBase;

import dev.Inventory.ClassesDTO.SQLiteDB;

import java.sql.*;

public class DatabaseConnection {
    // Database connection URL, username, and password


    // Method to establish and return a connection to the database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = SQLiteDB.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main() {
        String insertSQL = "INSERT INTO SupplyDays (dayName) VALUES (?)";
        String[] days = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Loop through each day and insert into the database
            for (String day : days) {
                pstmt.setString(1, day);
                pstmt.executeUpdate();
            }

            System.out.println("Days inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
