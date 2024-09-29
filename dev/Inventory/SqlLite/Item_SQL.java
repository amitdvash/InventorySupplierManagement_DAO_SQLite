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
        String sql = "INSERT INTO items (name, category, sub_category, size, cost_price, selling_price, priceAfterDiscount, manufacturer, expiry_date, status, place) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getCategory());
            pstmt.setString(3, entity.getSubCategory());
            pstmt.setDouble(4, entity.getSize());
            pstmt.setDouble(5, entity.getCostPrice());
            pstmt.setDouble(6, entity.getSellingPrice());
            pstmt.setDouble(7, entity.getPriceAfterDiscount());  // Add price after discount
            pstmt.setString(8, entity.getManufacturer());

            if (entity.getExpiryDate() != null) {
                pstmt.setString(9, entity.getExpiryDate().toString());
            } else {
                pstmt.setNull(9, Types.VARCHAR);
            }

            pstmt.setString(10, entity.getStatus().toString());
            pstmt.setString(11, entity.getPlace().toString());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entity.setId(id);  // Set the ID on the entity
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean update(Item entity) {
        String sql = "UPDATE items SET cost_price = ?, selling_price = ?, priceAfterDiscount = ?, " +
                "manufacturer = ?, expiry_date = ?, status = ?, place = ? " +
                "WHERE name = ? AND category = ? AND sub_category = ? AND size = ? AND place = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, entity.getCostPrice());
            pstmt.setDouble(2, entity.getSellingPrice());
            pstmt.setDouble(3, entity.getPriceAfterDiscount());  // Add price after discount
            pstmt.setString(4, entity.getManufacturer());

            if (entity.getExpiryDate() != null) {
                pstmt.setString(5, entity.getExpiryDate().toString());
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }

            pstmt.setString(6, entity.getStatus().toString());
            pstmt.setString(7, entity.getPlace().toString());

            // Set the WHERE clause values
            pstmt.setString(8, entity.getName());
            pstmt.setString(9, entity.getCategory());
            pstmt.setString(10, entity.getSubCategory());
            pstmt.setDouble(11, entity.getSize());
            pstmt.setString(12, entity.getPlace().toString());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Item item) {
        // First, we find the row with the minimum ROWID that matches the criteria
        String selectRowIdSql = "SELECT ROWID FROM items WHERE name = ? AND category = ? AND sub_category = ? AND size = ? AND place = ? LIMIT 1";
        String deleteSql = "DELETE FROM items WHERE ROWID = ?";  // Deleting by ROWID ensures only one record is deleted

        try (PreparedStatement selectStmt = connection.prepareStatement(selectRowIdSql);
             PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {

            // Set the values for the parameters in the prepared statement for the rowid query
            selectStmt.setString(1, item.getName());
            selectStmt.setString(2, item.getCategory());
            selectStmt.setString(3, item.getSubCategory());
            selectStmt.setDouble(4, item.getSize());
            selectStmt.setString(5, item.getPlace().toString());

            // Execute the query to find the ROWID
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                long rowId = rs.getLong(1);  // Get the ROWID of the first matching record

                // Now delete the record with that specific ROWID
                deleteStmt.setLong(1, rowId);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("One item deleted successfully.");
                    return true; // One item was deleted
                } else {
                    System.out.println("Failed to delete the item.");
                    return false; // Failed to delete
                }
            } else {
                System.out.println("Item not found.");
                return false; // No matching item found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of an error
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

    // New methods

    // Read an item by its ID
    public Item readById(int id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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
                        expiryDate,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Read all items that match a specific product (by name, category, sub_category, size)
    public List<Item> readAllByProduct(String name, String category, String sub_category, double size) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name = ? AND category = ? AND sub_category = ? AND size = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, sub_category);
            pstmt.setDouble(4, size);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
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
                        expiryDate,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Read all items by product and status
    public List<Item> readAllByProductAndStatus(String name, String category, String sub_category, double size, E_Item_Status status) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name = ? AND category = ? AND sub_category = ? AND size = ? AND status = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, sub_category);
            pstmt.setDouble(4, size);
            pstmt.setString(5, status.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
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
                        expiryDate,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Read all items by product and place
    public List<Item> readAllByProductAndPlace(String name, String category, String sub_category, double size, E_Item_Place place) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name = ? AND category = ? AND sub_category = ? AND size = ? AND place = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, sub_category);
            pstmt.setDouble(4, size);
            pstmt.setString(5, place.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
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
                        expiryDate,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Read all items by place
    public List<Item> readAllByPlace(E_Item_Place place) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE place = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, place.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
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
                        expiryDate,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Read all items by status
    public List<Item> readAllByStatus(E_Item_Status status) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE status = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
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
                        expiryDate,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
                item.setId(rs.getInt("id"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    public Item readByCompositeKey(String name, String category, String subCategory, double size, E_Item_Place place) {
        String sql = "SELECT * FROM items WHERE name = ? AND category = ? AND sub_category = ? AND size = ?  AND place = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, subCategory);
            pstmt.setDouble(4, size);
            pstmt.setString(5, place.toString());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Item(
                        rs.getString("name"),
                        rs.getDouble("cost_price"),
                        rs.getDouble("selling_price"),
                        rs.getString("manufacturer"),
                        rs.getString("category"),
                        rs.getString("sub_category"),
                        rs.getDouble("size"),
                        rs.getString("expiry_date") != null ? LocalDate.parse(rs.getString("expiry_date")) : null,
                        E_Item_Status.valueOf(rs.getString("status")),
                        E_Item_Place.valueOf(rs.getString("place"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update_new(Item entity, E_Item_Place oldPlace) {
        // First, select the ROWID of one specific record that matches the criteria
        String selectSql = "SELECT ROWID FROM items WHERE name = ? AND category = ? AND sub_category = ? AND size = ? AND place = ? LIMIT 1";
        String updateSql = "UPDATE items SET cost_price = ?, selling_price = ?, " +
                "manufacturer = ?, expiry_date = ?, status = ?, place = ? " +
                "WHERE ROWID = ?";  // Update only the row identified by ROWID

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

            // Set the values to select the specific item using the old place
            selectStmt.setString(1, entity.getName());
            selectStmt.setString(2, entity.getCategory());
            selectStmt.setString(3, entity.getSubCategory());
            selectStmt.setDouble(4, entity.getSize());
            selectStmt.setString(5, oldPlace.toString());

            // Execute the select query to get the ROWID
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                long rowId = rs.getLong(1);  // Get the ROWID of the item to update

                // Now set the update parameters to update this specific row
                updateStmt.setDouble(1, entity.getCostPrice());
                updateStmt.setDouble(2, entity.getSellingPrice());
                updateStmt.setString(3, entity.getManufacturer());

                if (entity.getExpiryDate() != null) {
                    updateStmt.setString(4, entity.getExpiryDate().toString());
                } else {
                    updateStmt.setNull(4, Types.VARCHAR);  // Set as NULL if expiry_date is null
                }

                updateStmt.setString(5, entity.getStatus().toString());
                updateStmt.setString(6, entity.getPlace().toString());  // New place
                updateStmt.setLong(7, rowId);  // Use the specific ROWID to ensure only one record is updated

                // Execute the update
                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected == 1;  // Return true if one record was updated
            } else {
                System.out.println("No matching record found to update.");
                return false;  // No record found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
