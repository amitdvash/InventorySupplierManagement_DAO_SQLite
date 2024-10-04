package dev.Suppliers.DataBase;

import java.sql.*;

public class DatabaseConnection {
    // Database connection URL, username, and password
    private static final String URL = "jdbc:postgresql://localhost:5432/SuppliersDatabase"; // Replace 'localhost' if your database is on a different server
    private static final String USER = "postgres"; // Replace with your PostgreSQL username
    private static final String PASSWORD = "1234"; // Replace with your PostgreSQL password

    // Method to establish and return a connection to the database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        return conn;
    }

    // Main method to test the connection
    public static void main(String[] args) {
        try (Connection connection = connect()) {
            if (connection != null) {
                // Insert data into the Suppliers table
                String insertSupplierSQL = "INSERT INTO Suppliers (name, phone, email, companyID, bankAccount, paymentMethod) VALUES (?, ?, ?, ?, ?, ?) RETURNING supplierID";
                int supplierID = 0;
                try (PreparedStatement pstmt = connection.prepareStatement(insertSupplierSQL)) {
                    pstmt.setString(1, "Supplier One");
                    pstmt.setString(2, "123456789");
                    pstmt.setString(3, "supplier1@example.com");
                    pstmt.setString(4, "COMP001");
                    pstmt.setString(5, "1234567890");
                    pstmt.setString(6, "Credit Card");

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        supplierID = rs.getInt("supplierID"); // Retrieve the generated supplierID
                        System.out.println("Inserted into Suppliers table successfully. SupplierID: " + supplierID);
                    }
                }

                // Step 2: Insert data into the Agreements table
                String insertAgreementSQL = "INSERT INTO Agreements (supplierID, productList, discountDetails, supplyDays, selfSupply) VALUES (?, ?, ?, ?, ?) RETURNING agreementID";
                int agreementID = 0;
                try (PreparedStatement pstmt = connection.prepareStatement(insertAgreementSQL)) {
                    pstmt.setInt(1, supplierID); // Use the generated supplierID
                    pstmt.setString(2, "Product One, Product Two");
                    pstmt.setString(3, "10% off for 10 units or more");
                    pstmt.setString(4, "Monday, Wednesday");
                    pstmt.setBoolean(5, true);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        agreementID = rs.getInt("agreementID"); // Retrieve the generated agreementID
                        System.out.println("Inserted into Agreements table successfully. AgreementID: " + agreementID);
                    }
                }

                // Step 3: Insert data into the Products table using the generated agreementID
                String insertProductSQL = "INSERT INTO Products (name, price, expirationDays, weight, agreementID) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertProductSQL)) {
                    pstmt.setString(1, "Product One");
                    pstmt.setDouble(2, 10.50);
                    pstmt.setInt(3, 30);
                    pstmt.setDouble(4, 1.5);
                    pstmt.setInt(5, agreementID); // Use the generated agreementID
                    pstmt.executeUpdate();
                    System.out.println("Inserted into Products table successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
