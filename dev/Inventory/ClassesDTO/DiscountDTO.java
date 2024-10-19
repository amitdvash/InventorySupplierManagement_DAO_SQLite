package dev.Inventory.ClassesDTO;

import dev.Inventory.Classes.Discount;
import dev.Inventory.Interface.IDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiscountDTO implements IDTO<Discount> {
    private Connection connection;

    public DiscountDTO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Discount discount) {
        String sql = "INSERT INTO discounts (discount_rate, start_date, end_date) " +
                "VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, discount.getDiscountRate());
            pstmt.setString(2, discount.getStartDate().toString());
            pstmt.setString(3, discount.getEndDate().toString());

            // Execute the insert
            int rowsAffected = pstmt.executeUpdate();

            // Get the generated discount ID and set it on the discount object
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    discount.setId(id);
                }
            }

            return rowsAffected > 0; // Return true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Discount discount) {
        String sql = "UPDATE discounts SET discount_rate = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, discount.getDiscountRate());
            pstmt.setString(2, discount.getStartDate().toString());
            pstmt.setString(3, discount.getEndDate().toString());
            pstmt.setInt(4, discount.getId());

            // Execute the update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Discount discount) {
        String sql = "DELETE FROM discounts WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, discount.getId());

            // Execute the delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Discount> readAll() {
        List<Discount> discounts = new ArrayList<>();
        String sql = "SELECT * FROM discounts";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Discount discount = buildDiscountFromResultSet(rs);
                discounts.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }

    // Helper method to read a discount by ID
    public Discount readById(int id) {
        String sql = "SELECT * FROM discounts WHERE id = ?";
        Discount discount = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                discount = buildDiscountFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }

    // Helper method to build a Discount object from a ResultSet
    private Discount buildDiscountFromResultSet(ResultSet rs) throws SQLException {
        return new Discount(
                rs.getInt("id"),
                rs.getDouble("discount_rate"),
                LocalDate.parse(rs.getString("start_date")),
                LocalDate.parse(rs.getString("end_date"))
        );
    }

    // Method to apply discount to all products in a category using a SQL query
    public void updateCategoryPrices(String category, double discountPercentage) throws SQLException {
        String sql = "UPDATE items " +
                "SET priceAfterDiscount = selling_price * ? " +
                "WHERE category = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Calculate the discount factor
            double discountFactor = (100 - discountPercentage) / 100;

            // Set the discount factor and the category in the query
            pstmt.setDouble(1, discountFactor);
            pstmt.setString(2, category);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Updated prices for " + rowsAffected + " items in category " + category);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void updatePricesByName(String name, double discountPercentage) throws SQLException {
        String sql = "UPDATE items " +
                "SET priceAfterDiscount = selling_price * ? " +
                "WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Calculate the discount factor
            double discountFactor = (100 - discountPercentage) / 100;

            // Set the discount factor and the product name in the query
            pstmt.setDouble(1, discountFactor);
            pstmt.setString(2, name);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Updated prices for " + rowsAffected + " items with name " + name);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void updateSubCategoryPrices(String subCategory, double discountPercentage) throws SQLException {
        String sql = "UPDATE items " +
                "SET priceAfterDiscount = selling_price * ? " +
                "WHERE sub_category = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Calculate the discount factor
            double discountFactor = (100 - discountPercentage) / 100;

            // Set the discount factor and the sub-category in the query
            pstmt.setDouble(1, discountFactor);
            pstmt.setString(2, subCategory);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Updated prices for " + rowsAffected + " items in sub-category " + subCategory);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // DiscountDTO class
    public Discount findDiscountByCategoryOrSubCategory(String category, String subCategory) throws SQLException {
        String sql = "SELECT * FROM discounts " +
                "WHERE (category = ?) " +
                "OR (sub_category = ?) " +
                "LIMIT 1";  // We prioritize the match by category or subcategory

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Bind the parameters for category and subcategory
            pstmt.setString(1, category);
            pstmt.setString(2, subCategory);

            ResultSet rs = pstmt.executeQuery();

            // If a discount is found, return the Discount object
            if (rs.next()) {
                int discountRate = rs.getInt("discount_rate");
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();

                // Create a discount object
                return new Discount(discountRate, startDate, endDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if no discount is found
        return null;
    }
}
