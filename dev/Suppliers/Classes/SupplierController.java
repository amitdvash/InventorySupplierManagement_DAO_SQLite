package dev.Suppliers.Classes;

import java.util.ArrayList;
import java.util.List;

public class SupplierController {
    private List<Supplier> suppliers = new ArrayList<>();

    // Register a new supplier with contact information
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
        System.out.println("Supplier added: " + supplier.getSupplierID());
    }

    // Update an existing supplier
    public void updateSupplier(String supplierID, Supplier updatedSupplier) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID)) {
                supplier.setCompanyID(updatedSupplier.getCompanyID());
                supplier.setBankAccount(updatedSupplier.getBankAccount());
                supplier.setPaymentMethod(updatedSupplier.getPaymentMethod());
                supplier.setProductList(updatedSupplier.getProductList());
                supplier.setSupplierAgreement(updatedSupplier.getSupplierAgreement());
                supplier.setContact(updatedSupplier.getContact()); // Update contact information
                System.out.println("Supplier updated: " + supplierID);
                return;
            }
        }
        System.out.println("Supplier not found: " + supplierID);
    }

    // Get a supplier by ID
    public Supplier getSupplier(String supplierID) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID)) {
                return supplier;
            }
        }
        System.out.println("Supplier not found: " + supplierID);
        return null;
    }

    // Get all suppliers
    public List<Supplier> getAllSuppliers() {
        return suppliers;
    }

    // Add an agreement to a supplier
    public void addAgreementToSupplier(String supplierID, Agreement agreement) {
        Supplier supplier = getSupplier(supplierID);
        if (supplier != null) {
            supplier.setSupplierAgreement(agreement);
            System.out.println("Agreement added to supplier: " + supplierID);
        } else {
            System.out.println("Supplier not found: " + supplierID);
        }
    }

    // Update supplier contact information
    public void updateSupplierContact(String supplierID, SupplierContact newContact) {
        Supplier supplier = getSupplier(supplierID);
        if (supplier != null) {
            supplier.setContact(newContact);
            System.out.println("Supplier contact updated for supplierID: " + supplierID);
        } else {
            System.out.println("Supplier not found: " + supplierID);
        }
    }

    // Get the contact information for a supplier
    public SupplierContact getSupplierContact(String supplierID) {
        Supplier supplier = getSupplier(supplierID);
        if (supplier != null) {
            return supplier.getContact();
        } else {
            System.out.println("Supplier not found: " + supplierID);
            return null;
        }
    }
}
