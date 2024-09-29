package dev.Inventory.SqlLite;

import dev.Inventory.Classes.Product;
import dev.Inventory.Classes.Discount;
import dev.Inventory.Classes.Item;
import dev.Inventory.Enums.E_Product_Status;
import dev.Inventory.Interface.IDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSQL implements IDTO<Product> {
    private Connection connection;
    private Discount_SQL discountSQL;
    private Item_SQL itemSQL;

    public ProductSQL(Connection connection) {
        this.connection = connection;
        this.discountSQL = new Discount_SQL(connection); // Initialize DiscountSQL for handling discounts
        this.itemSQL = new Item_SQL(connection); // Initialize ItemSQL for handling items
    }

    @Override
    public boolean create(Product product) {
        // First, check if a product with the same composite key already exists
        if (readByCompositeKey(product.getName(), product.getCategory(), product.getSub_category(), product.getSize()) != null) {
            System.out.println("Error: Product with the same name, category, sub-category, and size already exists.");
            return false;  // Indicate that the product was not inserted
        }

        String sql = "INSERT INTO products (name, category, sub_category, size, min_quantity, status, discount_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set values for product insertion
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setString(3, product.getSub_category());
            pstmt.setDouble(4, product.getSize());
            pstmt.setInt(5, product.getMin_quantity());
            pstmt.setString(6, product.getStatus().toString());

            // Set the discount ID if the product has a discount, else set null
            if (product.getDiscount() != null && product.getDiscount().getId() != 0) {
                pstmt.setInt(7, product.getDiscount().getId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            // Insert product into the products table
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET min_quantity = ?, status = ?, discount_id = ?, quantity = ? WHERE name = ? AND category = ? AND sub_category = ? AND size = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set values for product update
            pstmt.setInt(1, product.getMin_quantity());
            pstmt.setString(2, product.getStatus().toString());

            // Set the discount ID if the product has a discount, else set null
            if (product.getDiscount() != null && product.getDiscount().getId() != 0) {
                pstmt.setInt(3, product.getDiscount().getId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            // Set the quantity value (how many items this product has)
            pstmt.setInt(4, product.getQuantity_in_store()+product.getQuantity_in_warehouse());

            // Set the remaining values for product identification
            pstmt.setString(5, product.getName());
            pstmt.setString(6, product.getCategory());
            pstmt.setString(7, product.getSub_category());
            pstmt.setDouble(8, product.getSize());

            // Execute product update
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Product product) {
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
        return false;
    }

    @Override
    public List<Product> readAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Product product = buildProductFromResultSet(rs);
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
                product = buildProductFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    // Helper method to read all products in a specific category
    public List<Product> readAllByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = buildProductFromResultSet(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Helper method to read all products in a specific sub-category
    public List<Product> readAllBySubCategory(String subCategory) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE sub_category = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, subCategory);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = buildProductFromResultSet(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    // Helper method to build a Product from a ResultSet, including discount and items
    private Product buildProductFromResultSet(ResultSet rs) throws SQLException {
        // Retrieve the basic product fields
        Product product = new Product(
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("sub_category"),
                rs.getDouble("size"),
                rs.getInt("min_quantity"),
                null  // The discount will be loaded below
        );

        // Retrieve the discount if applicable
        int discountId = rs.getInt("discount_id");
        if (discountId != 0) {
            Discount discount = discountSQL.readById(discountId);
            product.setDiscount(discount);
        }

        // Retrieve the associated items
        List<Item> items = itemSQL.readAllByProduct(product.getName(),product.getCategory(),product.getSub_category(),product.getSize());
        for (Item item : items) {
            product.addItem(item);
        }

        return product;
    }

    public List<Product> readAllByStatus(E_Product_Status status) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE status = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status.toString());  // Set the status as a string in the prepared statement
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = buildProductFromResultSet(rs);  // Build the product using the helper method
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> readByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set the name parameter in the query
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            // Loop through the result set and create Product objects
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("sub_category"),
                        rs.getDouble("size"),
                        rs.getInt("min_quantity"),
                        null  // Assuming discount will be loaded separately
                );

                // Optionally, you can add logic to retrieve and set the discount if it's available
                int discountId = rs.getInt("discount_id");
                if (discountId != 0) {
                    Discount discount = discountSQL.readById(discountId);  // Assuming there's a method to read discounts by ID
                    product.setDiscount(discount);
                }

                // Add the product to the list
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

}
