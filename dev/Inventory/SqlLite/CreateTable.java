package dev.Inventory.SqlLite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    // Method to drop the products table if it exists
    public static void dropTable(Connection conn, String tableName) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(tableName + " table dropped successfully.");
        } catch (SQLException e) {
            System.out.println("Error dropping " + tableName + " table: " + e.getMessage());
            throw e;
        }
    }

    // Method to create the products table with a 'quantity' column
    public static void createProductsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "sub_category TEXT NOT NULL, " +
                "size REAL NOT NULL, " +
                "min_quantity INTEGER, " +
                "quantity INTEGER DEFAULT 0, " +  // New column to track the number of products
                "status TEXT NOT NULL, " +
                "discount_id INTEGER, " +  // Foreign key column to discounts
                "UNIQUE(name, category, sub_category, size), " +
                "FOREIGN KEY(discount_id) REFERENCES discounts(id)" +  // Foreign key constraint
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Products table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Products table: " + e.getMessage());
            throw e;
        }
    }


    // Method to create the items table
    public static void createItemsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "sub_category TEXT NOT NULL, " +
                "size REAL NOT NULL, " +
                "cost_price REAL NOT NULL, " +
                "selling_price REAL NOT NULL, " +
                "priceAfterDiscount REAL, " +  // New column for price after applying the discount
                "manufacturer TEXT NOT NULL, " +
                "expiry_date TEXT, " +
                "status TEXT NOT NULL, " +
                "place TEXT NOT NULL, " +
                "product_id INTEGER, " +
                "FOREIGN KEY(product_id) REFERENCES products(rowid)" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Items table created with price_after_discount column.");
        } catch (SQLException e) {
            System.out.println("Error creating Items table: " + e.getMessage());
            throw e;
        }
    }


    // Method to create the discounts table
    public static void createDiscountsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS discounts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "discount_rate REAL NOT NULL, " +
                "start_date TEXT NOT NULL, " +
                "end_date TEXT NOT NULL" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Discounts table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating Discounts table: " + e.getMessage());
            throw e;
        }
    }

    // Method to initialize all tables
    public static void initializeTables(Connection conn) throws SQLException {
        try {
            // Wrap in transaction to ensure atomicity
            conn.setAutoCommit(false);

            // Create the necessary tables
            createDiscountsTable(conn);  // Create the discounts table
            createProductsTable(conn);   // Create the products table
            createItemsTable(conn);      // Create the items table

            // Commit the transaction
            conn.commit();
            System.out.println("All tables created successfully.");
        } catch (SQLException e) {
            conn.rollback();  // Rollback transaction in case of failure
            System.out.println("Error initializing tables: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true);  // Restore auto-commit to true
        }
    }
}
