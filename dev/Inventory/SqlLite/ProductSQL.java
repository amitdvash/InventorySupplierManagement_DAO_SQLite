package dev.Inventory.SqlLite;

import dev.Inventory.Classes.Product;
import dev.Inventory.Interface.IDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSQL implements IDTO<Product> {
    private Connection connection;

    public ProductSQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Product product) {
        String sql = "INSERT OR IGNORE INTO products (name, category, sub_category, size, min_quantity, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set values for product insertion
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setString(3, product.getSub_category());
            pstmt.setDouble(4, product.getSize());
            pstmt.setInt(5, product.getMin_quantity());
            pstmt.setString(6, product.getStatus().toString());

            // Insert product into the products table
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was inserted, false if ignored
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET min_quantity = ?, status = ? WHERE name = ? AND category = ? AND sub_category = ? AND size = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set values for product update
            pstmt.setInt(1, product.getMin_quantity());
            pstmt.setString(2, product.getStatus().toString());
            pstmt.setString(3, product.getName());
            pstmt.setString(4, product.getCategory());
            pstmt.setString(5, product.getSub_category());
            pstmt.setDouble(6, product.getSize());

            // Execute product update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void delete(Product product) {
        String sql = "DELETE FROM products WHERE name = ? AND category = ? AND sub_category = ? AND size = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setString(3, product.getSub_category());
            pstmt.setDouble(4, product.getSize());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> readAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("sub_category"),
                        rs.getDouble("size"),
                        rs.getInt("min_quantity"),
                        null  // Discount and items not included here
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Helper method to read a single product by its composite key
    public Product readByCompositeKey(String name, String category, String sub_category, double size) {
        String sql = "SELECT * FROM products WHERE name = ? AND category = ? AND sub_category = ? AND size = ?";
        Product product = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, sub_category);
            pstmt.setDouble(4, size);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("sub_category"),
                        rs.getDouble("size"),
                        rs.getInt("min_quantity"),
                        null  // Discount and items not included
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }
}
