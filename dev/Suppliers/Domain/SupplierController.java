package dev.Suppliers.Domain;

import dev.Suppliers.Enums.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class SupplierController {
    private List<Supplier> suppliers;

    public SupplierController() {
        suppliers = new ArrayList<>();
    }

    // Add a supplier
    public Supplier createSupplier(String companyID, String bankAccount, PaymentMethod paymentMethod, Agreement supplierAgreement, String name, String phoneNumber, String email) {
        SupplierContact contact = new SupplierContact(name, phoneNumber, email);
        Supplier supplier = new Supplier(companyID, bankAccount, paymentMethod, supplierAgreement, contact);
        if (supplier.getSupplierAgreement() != null ){
            supplier.getSupplierAgreement().setSupplier(supplier);
        }
        suppliers.add(supplier);
        System.out.println("Supplier created: " + supplier.getSupplierID());
        return supplier;
    }

    // Add a product to a supplier's agreement
    public void addProductToSupplier(Supplier supplier, Product product) {
        Agreement agreement = supplier.getSupplierAgreement();
        if (agreement != null) {
            agreement.addProduct(product);
            System.out.println("Product " + product.getName() + " added to supplier " + supplier.getContact().getName());
        } else {
            System.out.println("Supplier has no agreement.");
        }
    }

    // Delete a product from a supplier's agreement
    public void deleteProductFromSupplier(Supplier supplier, String productID) {
        Agreement agreement = supplier.getSupplierAgreement();
        if (agreement != null) {
            Product productToDelete = null;
            for (Product product : agreement.getProductList()) {
                if (product.getCatalogID().equals(productID)) {
                    productToDelete = product;
                    break;
                }
            }
            if (productToDelete != null) {
                agreement.getProductList().remove(productToDelete);
                agreement.getDiscountDetails().remove(productID);
                System.out.println("Product " + productToDelete.getName() + " deleted from supplier " + supplier.getContact().getName());
            } else {
                System.out.println("Product not found.");
            }
        } else {
            System.out.println("Supplier has no agreement.");
        }
    }

    // Update supplier's payment method
    public void updatePaymentMethod(String supplierID, PaymentMethod paymentMethod) {
        Supplier supplier = getSupplierById(supplierID);
        if (supplier != null) {
            supplier.setPaymentMethod(paymentMethod);
            System.out.println("Updated payment method for supplier: " + supplierID);
        } else {
            System.out.println("Supplier not found: " + supplierID);
        }
    }

    // Get supplier by ID
    public Supplier getSupplierById(String supplierID) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID)) {
                return supplier;
            }
        }
        return null;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void deleteSupplier(String supplierID) {
        Supplier supplierToDelete = getSupplierById(supplierID);
        if (supplierToDelete != null) {
            suppliers.remove(supplierToDelete);
            System.out.println("Supplier deleted: " + supplierID);
        } else {
            System.out.println("Supplier not found: " + supplierID);
        }
    }

    /*

    // Validate Company ID
    public boolean isValidCompanyID(String companyID) {
        return companyID.matches("\\d+");
    }

    // Validate Bank Account
    public boolean isValidBankAccount(String bankAccount) {
        return bankAccount.matches("\\d+");
    }

    // Validate Phone Number
    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d+"); // Adjust regex as needed
    }

    // Validate Email
    public boolean isValidEmail(String email) {
        // Simplified regex for email validation
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Update Supplier Details
    public void updateCompanyID(Supplier supplier, String companyID) {
        if (isValidCompanyID(companyID)) {
            supplier.setCompanyID(companyID);
        }
    }

    public void updateBankAccount(Supplier supplier, String bankAccount) {
        if (isValidBankAccount(bankAccount)) {
            supplier.setBankAccount(bankAccount);
        }
    }

    public void updatePaymentMethod(Supplier supplier, PaymentMethod paymentMethod) {
        supplier.setPaymentMethod(paymentMethod);
    }

    public void updateContactInfo(Supplier supplier, SupplierContact contact) {
        supplier.setContact(contact);
    }

     */
}
