package dev.Suppliers.DataBase;

import dev.Suppliers.Domain.Agreement;
import dev.Suppliers.Domain.Product;
import dev.Suppliers.Interfaces.IDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgreementDTO implements IDTO<Agreement> {
    private Connection connection;

    public AgreementDTO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int create(Agreement agreement) {
        String sql = "INSERT INTO Agreements (supplierID, selfSupply) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, agreement.getSupplierID());
            pstmt.setBoolean(2, agreement.isSelfSupply());
            pstmt.executeUpdate();

            // Get the generated agreementID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int agreementID = rs.getInt(1);
                agreement.setAgreementID(agreementID); // Set the generated agreementID in the Agreement object

                // Insert supply days into AgreementSupplyDays table
                insertAgreementSupplyDays(agreementID, agreement.getSupplyDays());

                return agreementID; // Return the generated agreementID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if creation failed
    }

    // Helper method to insert supply days into AgreementSupplyDays table
    private void insertAgreementSupplyDays(int agreementID, List<String> supplyDays) throws SQLException {
        String sql = "INSERT INTO AgreementSupplyDays (agreementID, dayName) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String day : supplyDays) {
                pstmt.setInt(1, agreementID);
                pstmt.setString(2, day);
                pstmt.addBatch(); // Add to batch to execute in a single transaction
            }
            pstmt.executeBatch(); // Execute the batch
        }
    }
    @Override
    public List<Agreement> readAll() {
        List<Agreement> agreements = new ArrayList<>();
        String sql = "SELECT * FROM Agreements";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Agreement agreement = new Agreement(
                        rs.getInt("agreementID"),
                        rs.getInt("supplierID"),
                        getProductsForAgreement(rs.getInt("agreementID")),
                        getSupplyDaysForAgreement(rs.getInt("agreementID")),
                        rs.getBoolean("selfSupply")
                );
                agreements.add(agreement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agreements;
    }

    @Override
    public void update(Agreement agreement) {
        String sql = "UPDATE Agreements SET selfSupply = ? WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, agreement.isSelfSupply());
            pstmt.setInt(2, agreement.getAgreementID());
            pstmt.executeUpdate();

            updateProducts(agreement); // Update associated products
            updateSupplyDays(agreement); // Update associated supply days
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int agreementID) {
        String sql = "DELETE FROM Agreements WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agreementID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Agreement read(int agreementID) {
        String sql = "SELECT * FROM Agreements WHERE agreementID = ?";
        Agreement agreement = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agreementID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                agreement = new Agreement(
                        rs.getInt("agreementID"),
                        rs.getInt("supplierID"),
                        getProductsForAgreement(rs.getInt("agreementID")),
                        getSupplyDaysForAgreement(rs.getInt("agreementID")),
                        rs.getBoolean("selfSupply")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agreement;
    }

    // Insert products associated with the agreement into the database
    private void insertProducts(Agreement agreement) {
        String sql = "INSERT INTO Products (name, price, expirationDays, weight, agreementID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Product product : agreement.getProductList()) {
                pstmt.setString(1, product.getName());
                pstmt.setDouble(2, product.getPrice());
                pstmt.setInt(3, product.getExpirationDays());
                pstmt.setDouble(4, product.getWeight());
                pstmt.setInt(5, agreement.getAgreementID());
                pstmt.addBatch(); // Add to batch
            }
            pstmt.executeBatch(); // Execute batch insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update products associated with the agreement in the database
    private void updateProducts(Agreement agreement) {
        // First delete old products
        String deleteSql = "DELETE FROM Products WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setInt(1, agreement.getAgreementID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert updated products
        insertProducts(agreement);
    }

    // Get the list of products associated with a specific agreement from the database
    private List<Product> getProductsForAgreement(int agreementID) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM ProductsSuppliers WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agreementID);
            ResultSet rs = pstmt.executeQuery();
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

    // Insert supply days associated with the agreement into the database
    private void insertSupplyDays(Agreement agreement) {
        String sql = "INSERT INTO AgreementSupplyDays (agreementID, dayName) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String day : agreement.getSupplyDays()) {
                pstmt.setInt(1, agreement.getAgreementID());
                pstmt.setString(2, day);
                pstmt.addBatch(); // Add to batch
            }
            pstmt.executeBatch(); // Execute batch insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update supply days associated with the agreement in the database
    private void updateSupplyDays(Agreement agreement) {
        // First delete old supply days
        String deleteSql = "DELETE FROM AgreementSupplyDays WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setInt(1, agreement.getAgreementID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert updated supply days
        insertSupplyDays(agreement);
    }

    // Get the list of supply days associated with a specific agreement from the database
    private List<String> getSupplyDaysForAgreement(int agreementID) {
        List<String> supplyDays = new ArrayList<>();
        String sql = "SELECT dayName FROM AgreementSupplyDays WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agreementID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                supplyDays.add(rs.getString("dayName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplyDays;
    }

    // Helper method to get product discount details from the productDiscounts table
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

    // Method to add a discount to a product in an agreement
    public void addDiscount(Agreement agreement, int catalogID, int quantity, double discountPercent) {
        HashMap<Integer, Double> discountDetails = getProductDiscountDetails(catalogID);
        discountDetails.put(quantity, discountPercent);

        String sql = "INSERT INTO productDiscounts (catalogID, quantity, discount) VALUES (?, ?, ?) " +
                "ON CONFLICT (catalogID, quantity) DO UPDATE SET discount = EXCLUDED.discount";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catalogID);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, discountPercent);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove a discount from a product in an agreement
    public void removeDiscount(Agreement agreement, int catalogID, int quantity) {
        String sql = "DELETE FROM productDiscounts WHERE catalogID = ? AND quantity = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catalogID);
            pstmt.setInt(2, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update discount details in an agreement
    public void updateDiscountDetails(Agreement agreement, int catalogID, HashMap<Integer, Double> newDiscountDetails) {
        // Remove existing discounts for the product
        String deleteSql = "DELETE FROM productDiscounts WHERE catalogID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
            pstmt.setInt(1, catalogID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Insert new discount details
        for (Map.Entry<Integer, Double> entry : newDiscountDetails.entrySet()) {
            addDiscount(agreement, catalogID, entry.getKey(), entry.getValue());
        }
    }
    public Agreement readBySupplierID(int supplierID) {
        Agreement agreement = null;
        String sql = "SELECT * FROM Agreements WHERE supplierID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                agreement = new Agreement(
                        rs.getInt("agreementID"),
                        rs.getInt("supplierID"),
                        getProductsForAgreement(rs.getInt("agreementID")),
                        getSupplyDaysForAgreement(rs.getInt("agreementID")),
                        rs.getBoolean("selfSupply")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agreement;
    }

    public void updateSelfSupplyInDatabase(int agreementID, boolean selfSupply) {
        String sql = "UPDATE Agreements SET selfSupply = ? WHERE agreementID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, selfSupply);
            pstmt.setInt(2, agreementID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSupplyDaysInDatabase(int agreementID, List<String> newSupplyDays) {
        String deleteSQL = "DELETE FROM AgreementSupplyDays WHERE agreementID = ?";
        String insertSQL = "INSERT INTO AgreementSupplyDays (agreementID, dayName) VALUES (?, ?)";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)) {
            deleteStmt.setInt(1, agreementID);
            deleteStmt.executeUpdate(); // Delete existing supply days
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
            for (String day : newSupplyDays) {
                insertStmt.setInt(1, agreementID);
                insertStmt.setString(2, day);
                insertStmt.addBatch();
            }
            insertStmt.executeBatch(); // Insert new supply days
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
