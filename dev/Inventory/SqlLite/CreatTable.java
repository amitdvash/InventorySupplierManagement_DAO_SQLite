package dev.Inventory.SqlLite;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class CreatTable {

    public static void createTables() {
        String createProductsTable = "CREATE TABLE IF NOT EXISTS products ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " name TEXT NOT NULL, "
                + " category TEXT, "
                + " sub_category TEXT, "
                + " size REAL, "
                + " min_quantity INTEGER "
                + ");";

        String createItemsTable = "CREATE TABLE IF NOT EXISTS items ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " name TEXT, "
                + " category TEXT, "
                + " product_id INTEGER, "
                + " sub_category TEXT, "
                + " size REAL, "
                + " cost_price REAL, "
                + " selling_price REAL, "
                + " manufacturer TEXT, "
                + " expiry_date TEXT, "
                + " status TEXT, "
                + " place TEXT, "
                + " FOREIGN KEY (product_id) REFERENCES products (id) "
                + ");";

        try (Connection conn = SQLiteDB.connect(); Statement stmt = conn.createStatement()) {
            // Check if the connection was successful
            if (conn == null) {
                System.out.println("Failed to establish database connection.");
                return;
            }

            // Execute SQL statements to create the tables
            System.out.println("Creating products table...");
            stmt.execute(createProductsTable);
            System.out.println("Products table created.");

            System.out.println("Creating items table...");
            stmt.execute(createItemsTable);
            System.out.println("Items table created.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
