package dev.Suppliers.DataBase;

import dev.Suppliers.Domain.Product;
import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Domain.SupplierContact;
import dev.Suppliers.Enums.PaymentMethod;
import dev.Suppliers.Interfaces.IDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDTO implements IDTO<Product> {
    private Connection connection;

    public ProductDTO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int create(Product product) {
        String sql = "INSERT INTO Products (name, price, expirationDays, weight, agreementID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getExpirationDays());
            pstmt.setDouble(4, product.getWeight());
            pstmt.setInt(5, product.getAgreement().getAgreementID());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int catalogID = rs.getInt(1); // Retrieve and store the generated catalogID

                // Insert discount details into productDiscounts table
                insertProductDiscounts(catalogID, product.getDiscountDetails());

                return catalogID; // Return the generated catalogID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if creation failed
    }

    // Helper method to insert discount details into the productDiscounts table
    private void insertProductDiscounts(int catalogID, HashMap<Integer, Double> discountDetails) {
        String sql = "INSERT INTO productDiscounts (catalogID, quantity, discount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<Integer, Double> entry : discountDetails.entrySet()) {
                pstmt.setInt(1, catalogID);
                pstmt.setInt(2, entry.getKey());
                pstmt.setDouble(3, entry.getValue());
                pstmt.addBatch(); // Add to batch for batch execution
            }
            pstmt.executeBatch(); // Execute all inserts in a single batch
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Product> readAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("catalogID"),
                        rs.getString("name"),
                        getProductDiscountDetails(rs.getInt("catalogID")),
                        rs.getDouble("price"),
                        rs.getInt("expirationDays"),
                        rs.getDouble("weight"),
                        null // Agreement object can be set if needed
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE Products SET name = ?, price = ?, expirationDays = ?, weight = ?, agreementID = ? WHERE catalogID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getExpirationDays());
            pstmt.setDouble(4, product.getWeight());
            pstmt.setInt(5, product.getAgreement() != null ? product.getAgreement().getAgreementID() : null);
            pstmt.setInt(6, product.getCatalogID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int catalogID) {
        String sql = "DELETE FROM Products WHERE catalogID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catalogID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find the cheapest supplier for a product and quantity using SQL queries
    public Supplier findCheapestSupplier(String productName, int quantity) {
        String sql = "SELECT s.* FROM Products p " +
                "JOIN productDiscounts pd ON p.catalogID = pd.catalogID " +
                "JOIN Agreements a ON p.agreementID = a.agreementID " +
                "JOIN Suppliers s ON a.supplierID = s.supplierID " +
                "WHERE p.name = ? AND pd.quantity <= ? " +
                "ORDER BY (p.price - (p.price * pd.discount / 100)) ASC " +
                "LIMIT 1";
        Supplier supplier = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            pstmt.setInt(2, quantity);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                supplier = new Supplier(
                        rs.getInt("supplierID"),
                        rs.getString("companyID"),
                        rs.getString("bankAccount"),
                        PaymentMethod.valueOf(rs.getString("paymentMethod")),
                        null,
                        new SupplierContact(rs.getString("name"), rs.getString("phoneNumber"), rs.getString("email"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

    // Get all product names from the database
    public List<String> getAllProductNames() {
        List<String> productNames = new ArrayList<>();
        String sql = "SELECT name FROM Products";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                productNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productNames;
    }


    // Method to get product discount details from the productDiscounts table
    private HashMap<Integer, Double> getProductDiscountDetails(int catalogID) {
        HashMap<Integer, Double> discountDetails = new HashMap<>();
        String sql = "SELECT * FROM productDiscounts WHERE catalogID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catalogID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                discountDetails.put(rs.getInt("quantity"), rs.getDouble("discount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discountDetails;
    }

    // Method to read product by name
    public Product readByName(String productName) {
        String sql = "SELECT * FROM Products WHERE name = ?";
        Product product = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                product = new Product(
                        rs.getInt("catalogID"),
                        rs.getString("name"),
                        getProductDiscountDetails(rs.getInt("catalogID")),
                        rs.getDouble("price"),
                        rs.getInt("expirationDays"),
                        rs.getDouble("weight"),
                        null // Agreement object can be set if needed
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void addDiscount(int catalogID, int quantity, double discount) {
        String sql = "INSERT INTO productDiscounts (catalogID, quantity, discount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catalogID);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, discount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
