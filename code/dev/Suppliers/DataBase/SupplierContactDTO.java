package dev.Suppliers.DataBase;

import dev.Suppliers.Domain.SupplierContact;
import dev.Suppliers.Interfaces.IDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierContactDTO implements IDTO<SupplierContact> {
    private Connection connection;

    public SupplierContactDTO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int create(SupplierContact contact) {
        // Implement create logic for SupplierContact
        return -1;
    }

    @Override
    public void update(SupplierContact contact) {
        // Implement update logic for SupplierContact
    }

    @Override
    public void delete(int supplierID) {
        // Implement delete logic for SupplierContact
    }

    @Override
    public List<SupplierContact> readAll() {
        // Implement readAll logic for SupplierContact
        return null;
    }

    public SupplierContact readBySupplierID(int supplierID) {
        String sql = "SELECT * FROM SupplierContacts WHERE supplierID = ?";
        SupplierContact contact = null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                contact = new SupplierContact(
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }
}
