package dev.Suppliers.DataBase;

import dev.Suppliers.DataBase.AgreementDTO;
import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Domain.Agreement;
import dev.Suppliers.Domain.SupplierContact;
import dev.Suppliers.Enums.PaymentMethod;
import dev.Suppliers.Interfaces.IDTO;
import dev.Suppliers.DataBase.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDTO implements IDTO<Supplier> {
    private Connection connection;

    public SupplierDTO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int create(Supplier supplier) {
        String sql = "INSERT INTO Suppliers (companyID, bankAccount, paymentMethod, name, phone, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, supplier.getCompanyID());
            pstmt.setString(2, supplier.getBankAccount());
            pstmt.setString(3, supplier.getPaymentMethod().name());
            pstmt.setString(4, supplier.getContact().getName());
            pstmt.setString(5, supplier.getContact().getPhoneNumber());
            pstmt.setString(6, supplier.getContact().getEmail());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1); // Return the generated supplierID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if creation failed
    }


    @Override
    public List<Supplier> readAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int supplierID = rs.getInt("supplierID");

                // Fetch Agreement and Contact Separately
                Agreement agreement = getAgreementBySupplierID(supplierID);
                SupplierContact contact = getSupplierContactBySupplierID(supplierID);

                Supplier supplier = new Supplier(
                        supplierID,
                        rs.getString("companyID"),
                        rs.getString("bankAccount"),
                        PaymentMethod.valueOf(rs.getString("paymentMethod")),
                        agreement,
                        contact
                );
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public Supplier read(int supplierID) {
        String sql = "SELECT * FROM Suppliers WHERE supplierID = ?";
        Supplier supplier = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Retrieve the Agreement and SupplierContact from their respective DTOs
                Agreement agreement = getAgreementBySupplierID(supplierID);
                SupplierContact contact = getSupplierContactBySupplierID(supplierID);

                supplier = new Supplier(
                        rs.getInt("supplierID"),
                        rs.getString("companyID"),
                        rs.getString("bankAccount"),
                        PaymentMethod.valueOf(rs.getString("paymentMethod")),
                        agreement, // Set the retrieved Agreement object
                        contact // Set the retrieved SupplierContact object
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

    @Override
    public void update(Supplier supplier) {
        String sql = "UPDATE Suppliers SET name = ?, phone = ?, email = ?, companyID = ?, bankAccount = ?, paymentMethod = ? WHERE supplierID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getContact().getName());
            pstmt.setString(2, supplier.getContact().getPhoneNumber());
            pstmt.setString(3, supplier.getContact().getEmail());
            pstmt.setString(4, supplier.getCompanyID());
            pstmt.setString(5, supplier.getBankAccount());
            pstmt.setString(6, supplier.getPaymentMethod().toString());
            pstmt.setInt(7, supplier.getSupplierID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int supplierID) {
        String sql = "DELETE FROM Suppliers WHERE supplierID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get Agreement by Supplier ID (placeholder, needs to be implemented)
    private Agreement getAgreementBySupplierID(int supplierID) {
        // Placeholder return, should return a valid Agreement object
        AgreementDTO agreementDTO = new AgreementDTO(connection);
        return agreementDTO.readBySupplierID(supplierID);
    }

    // Method to get SupplierContact by Supplier ID (placeholder, needs to be implemented)
    private SupplierContact getSupplierContactBySupplierID(int supplierID) {
        // Placeholder return, should return a valid SupplierContact object
        SupplierContactDTO supplierContactDTO = new SupplierContactDTO(connection);
        return supplierContactDTO.readBySupplierID(supplierID);
    }

    // Method to update the supplier's agreement in the database
    public void updateSupplierAgreement(int supplierID, int agreementID) {
        String sql = "UPDATE Suppliers SET agreementID = ? WHERE supplierID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, agreementID);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
