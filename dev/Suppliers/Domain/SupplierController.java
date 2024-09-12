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
    public Supplier addSupplier(String companyID, String bankAccount, PaymentMethod paymentMethod,
                            List<Product> productList, Agreement supplierAgreement, String name, String phoneNumber, String email) {
        SupplierContact contact = new SupplierContact(name, phoneNumber, email);
        Supplier supplier = new Supplier(companyID, bankAccount, paymentMethod, productList, supplierAgreement, contact);
        suppliers.add(supplier);
        System.out.println("Supplier added: " + supplier.getSupplierID());
        return supplier;
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
}
