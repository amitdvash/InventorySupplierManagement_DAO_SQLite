package dev.Inventory.SqlLite;

import dev.Inventory.Controllers.Controller_Menu;

import java.sql.Connection;
import java.time.LocalDate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Sql_helper {


    public static void addProduct_db(String name, String category, String subCategory, double size, int minQuantity) {
        String sql = "INSERT INTO products(name, category, sub_category, size, min_quantity) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = Controller_Menu.getSqlConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);
            ((PreparedStatement) pstmt).setString(2, category);
            pstmt.setString(3, subCategory);
            pstmt.setDouble(4, size);
            pstmt.setInt(5, minQuantity);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

//       Sql_helper.addItemToDB(colaItem2.getName(), colaItem2.getCost_price(),
//       colaItem2.getSelling_price(), colaItem2.getManufacturer(), colaItem2.getCategory(), colaItem2.getSub_category(),
    // Method to add an item to the database
public static void addItemToDB(String name, double costPrice, double sellingPrice, String manufacturer, String category,
                               String subCategory, double size, LocalDate expiryDate, String status, String place) {
    String sql = "INSERT INTO items(name, cost_price, selling_price, manufacturer, category, sub_category, size, expiry_date, status, place) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = Controller_Menu.getSqlConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set the values for the prepared statement
        pstmt.setString(1, name);
        pstmt.setDouble(2, costPrice);
        pstmt.setDouble(3, sellingPrice);
        pstmt.setString(4, manufacturer);
        pstmt.setString(5, category);
        pstmt.setString(6, subCategory);
        pstmt.setDouble(7, size);
        pstmt.setString(8, expiryDate.toString());
        pstmt.setString(9, status);
        pstmt.setString(10, place);

        // Execute the update
        pstmt.executeUpdate();
        System.out.println("Item added to database successfully.");

    } catch (SQLException e) {
        System.out.println("Error inserting item into database: " + e.getMessage());
    }
}



}
