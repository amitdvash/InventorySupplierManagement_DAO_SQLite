package dev.Suppliers.DataBase;

import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Domain.Product;
import dev.Suppliers.Domain.Order;
import dev.Suppliers.Enums.PaymentMethod;

import java.sql.*;

public class DatabaseManager {

    private Connection connect() {
        String url = "jdbc:sqlite:supplier_orders.db";  // SQLite database path
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Create the necessary tables
    public void createTables() {
        String createSuppliersTable = "CREATE TABLE IF NOT EXISTS Suppliers (" +
                "supplierID TEXT PRIMARY KEY, " +
                "companyID TEXT NOT NULL, " +
                "bankAccount TEXT NOT NULL, " +
                "paymentMethod TEXT NOT NULL, " +
                "contactName TEXT, " +
                "contactPhone TEXT, " +
                "contactEmail TEXT);";

        String createProductsTable = "CREATE TABLE IF NOT EXISTS Products (" +
                "catalogID TEXT PRIMARY KEY, " +
                "supplierID TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "expirationDays INTEGER NOT NULL, " +
                "weight REAL NOT NULL" +
                "FOREIGN KEY (supplierID) REFERENCES Suppliers(supplierID));";

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS Orders (" +
                "orderID TEXT PRIMARY KEY, " +
                "supplierID TEXT NOT NULL, " +
                "orderDate TEXT NOT NULL, " +
                "isConstantDelivery INTEGER NOT NULL, " +
                "totalPrice REAL, " +
                "FOREIGN KEY (supplierID) REFERENCES Suppliers(supplierID));";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createSuppliersTable);
            stmt.execute(createProductsTable);
            stmt.execute(createOrdersTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a supplier into the database
    public void insertSupplier(Supplier supplier) {
        String sql = "INSERT INTO Suppliers(supplierID, companyID, bankAccount, paymentMethod, contactName, contactPhone, contactEmail) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getSupplierID());
            pstmt.setString(2, supplier.getCompanyID());
            pstmt.setString(3, supplier.getBankAccount());
            pstmt.setString(4, supplier.getPaymentMethod().name());
            pstmt.setString(5, supplier.getContact().getName());
            pstmt.setString(6, supplier.getContact().getPhoneNumber());
            pstmt.setString(7, supplier.getContact().getEmail());
            pstmt.executeUpdate();
            System.out.println("Supplier inserted: " + supplier.getSupplierID());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a product into the database
    public void insertProduct(Product product) {
        String sql = "INSERT INTO Products(catalogID, name, price, expirationDays, weight) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getCatalogID());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getExpirationDays());
            pstmt.setDouble(5, product.getWeight());
            pstmt.executeUpdate();
            System.out.println("Product inserted: " + product.getCatalogID());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert an order into the database
    public void insertOrder(Order order) {
        String sql = "INSERT INTO Orders(orderID, supplierID, orderDate, isConstantDelivery, totalPrice) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getOrderID());
            pstmt.setString(2, order.getSupplier().getSupplierID());
            pstmt.setString(3, order.getOrderDate().toString());
            pstmt.setInt(4, order.isConstantDelivery() ? 1 : 0);
            pstmt.setDouble(5, order.getPriceAfterDiscount());
            pstmt.executeUpdate();
            System.out.println("Order inserted: " + order.getOrderID());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete a supplier by ID
    public void deleteSupplier(String supplierID) {
        String sql = "DELETE FROM Suppliers WHERE supplierID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplierID);
            pstmt.executeUpdate();
            System.out.println("Supplier deleted: " + supplierID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete a product by catalogID
    public void deleteProduct(String catalogID) {
        String sql = "DELETE FROM Products WHERE catalogID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, catalogID);
            pstmt.executeUpdate();
            System.out.println("Product deleted: " + catalogID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete an order by ID
    public void deleteOrder(String orderID) {
        String sql = "DELETE FROM Orders WHERE orderID = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderID);
            pstmt.executeUpdate();
            System.out.println("Order deleted: " + orderID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Helper method to close connection
    public void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
