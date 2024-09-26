package dev.Inventory.SqlLite;

import dev.Inventory.Classes.Item;
import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;
import dev.Inventory.Interface.IDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Item_SQL implements IDTO<Item> {
    private Connection connection;

    public Item_SQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(Item entity) {
        String sql = "INSERT INTO items (name, category, sub_category, size, cost_price, selling_price, manufacturer, expiry_date, status, place) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set non-nullable fields
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getCategory());
            pstmt.setString(3, entity.getSub_category());
            pstmt.setDouble(4, entity.getSize());
            pstmt.setDouble(5, entity.getCost_price());
            pstmt.setDouble(6, entity.getSelling_price());
            pstmt.setString(7, entity.getManufacturer());

            // Check if expiry_date is null
            if (entity.getExpiry_date() != null) {
                pstmt.setString(8, entity.getExpiry_date().toString());
            } else {
                pstmt.setNull(8, Types.VARCHAR);  // Set as NULL if expiry_date is null
            }

            pstmt.setString(9, entity.getStatus().toString());
            pstmt.setString(10, entity.getPlace().toString());

            // Execute the insert
            pstmt.executeUpdate();

            // Get the generated id and set it on the entity
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entity.setId(id);  // Set the id on the entity
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(Item entity) {
        String sql = "UPDATE items SET cost_price = ?, selling_price = ?, " +
                "manufacturer = ?, expiry_date = ?, status = ?, place = ? " +
                "WHERE id = ?";  // Update by id

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, entity.getCost_price());
            pstmt.setDouble(2, entity.getSelling_price());
            pstmt.setString(3, entity.getManufacturer());

            // Check if expiry_date is null
            if (entity.getExpiry_date() != null) {
                pstmt.setString(4, entity.getExpiry_date().toString());
            } else {
                pstmt.setNull(4, Types.VARCHAR);  // Set as NULL if expiry_date is null
            }

            pstmt.setString(5, entity.getStatus().toString());
            pstmt.setString(6, entity.getPlace().toString());

            pstmt.setInt(7, entity.getId());  // Update by the id

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Item item) {
        String sql = "DELETE FROM items WHERE id = ?";  // Delete by id
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Item> readAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Check if expiry_date is null
                LocalDate expiryDate = null;
                if (rs.getString("expiry_date") != null) {
                    expiryDate = LocalDate.parse(rs.getString("expiry_date"));
                }

                Item item = new Item(
                        rs.getString("name"),
                        rs.getDouble("cost_price"),
                        rs.getDouble("selling_price"),
                        rs.getString("manufacturer"),
                        rs.getString("category"),
                        rs.getString("sub_category"),
                        rs.getDouble("size"),
                        expiryDate,  // Set null if expiry_date was null
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));  // Set the id on the item
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
