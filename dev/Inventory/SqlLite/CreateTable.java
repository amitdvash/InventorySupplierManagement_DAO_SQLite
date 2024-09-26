package dev.Inventory.SqlLite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    // Method to create the products table
    public static void createProductsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "sub_category TEXT NOT NULL, " +
                "size REAL NOT NULL, " +
                "min_quantity INTEGER, " +
                "status TEXT NOT NULL, " +
                "UNIQUE(name, category, sub_category, size) " +  // Ensure unique combinations
                ");";

        try (Connection conn = SQLiteDB.connect(); Statement stmt = conn.createStatement()) {  // Correct connection and statement creation
            stmt.execute(sql);
            System.out.println("Products table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Products table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to create the items table
    public static void createItemsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Auto-incrementing primary key
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "sub_category TEXT NOT NULL, " +
                "size REAL NOT NULL, " +
                "cost_price REAL NOT NULL, " +
                "selling_price REAL NOT NULL, " +
                "manufacturer TEXT NOT NULL, " +
                "expiry_date TEXT, " +  // Allow null for expiry_date
                "status TEXT NOT NULL, " +
                "place TEXT NOT NULL, " +
                "UNIQUE(id) " +  // Ensure unique combinations for the item details
                ");";
        try (Connection conn = SQLiteDB.connect(); Statement stmt = conn.createStatement()) {  // Correct connection and statement creation
            stmt.execute(sql);
            System.out.println("Items table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Items table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to call both table creation methods
    public static void initializeTables() {
        createProductsTable();
        createItemsTable();
    }
}
