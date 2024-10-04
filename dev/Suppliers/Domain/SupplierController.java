package dev.Suppliers.Domain;

import dev.Suppliers.DataBase.AgreementDTO;
import dev.Suppliers.DataBase.ProductDTO;
import dev.Suppliers.DataBase.SupplierDTO;
import dev.Suppliers.Enums.PaymentMethod;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class SupplierController {
    private SupplierDTO supplierDTO;
    private ProductDTO productDTO;
    private AgreementDTO agreementDTO;

    public SupplierController(Connection connection) {
        this.supplierDTO = new SupplierDTO(connection);
        this.productDTO = new ProductDTO(connection);
        this.agreementDTO = new AgreementDTO(connection);
    }

    // Add a supplier to the database
    public Supplier createSupplier(String companyID, String bankAccount, String paymentMethod, Agreement agreement, String name, String phoneNumber, String email) {
        Supplier supplier = new Supplier(0, companyID, bankAccount, PaymentMethod.valueOf(paymentMethod), agreement, new SupplierContact(name, phoneNumber, email));
        int supplierID = supplierDTO.create(supplier); // Get supplierID from database
        supplier.setSupplierID(supplierID); // Set the generated supplierID to the supplier object
        System.out.println("Supplier created: " + supplierID);
        return supplier;
    }

    // Add a product to a supplier's agreement
    public void addProductToSupplier(Supplier supplier, Product product) {
        Agreement agreement = supplier.getSupplierAgreement();
        if (agreement != null) {
            agreement.addProduct(product);
            productDTO.create(product); // Add the product to the database
            agreementDTO.update(agreement); // Update the agreement in the database
            System.out.println("Product " + product.getName() + " added to supplier " + supplier.getContact().getName());
        } else {
            System.out.println("Supplier has no agreement.");
        }
    }

    // Delete a product from a supplier's agreement
    public void deleteProductFromSupplier(Supplier supplier, int productID) {
        Agreement agreement = supplier.getSupplierAgreement();
        if (agreement != null) {
            Product productToDelete = null;

            // Step 1: Find the product in the agreement's product list
            for (Product product : agreement.getProductList()) {
                if (product.getCatalogID() == productID) {
                    productToDelete = product;
                    break;
                }
            }

            if (productToDelete != null) {
                // Step 2: Remove the product from the agreement's product list
                agreement.getProductList().remove(productToDelete);

                // Step 3: Remove any discounts associated with this product (if applicable)
                productToDelete.setDiscountDetails(new HashMap<>()); // Clear the discount details of the product

                // Step 4: Update the product and agreement in the database
                productDTO.delete(productToDelete.getCatalogID()); // Delete the product from the database
                agreementDTO.update(agreement); // Update the agreement in the database

                System.out.println("Product " + productToDelete.getName() + " deleted from supplier " + supplier.getContact().getName());
            } else {
                System.out.println("Product not found.");
            }
        } else {
            System.out.println("Supplier has no agreement.");
        }
    }

    // Update supplier's payment method
    public void updatePaymentMethod(int supplierID, PaymentMethod paymentMethod) {
        Supplier supplier = getSupplierById(supplierID);
        if (supplier != null) {
            supplier.setPaymentMethod(paymentMethod);
            supplierDTO.update(supplier); // Update the database through the DTO
            System.out.println("Updated payment method for supplier: " + supplierID);
        } else {
            System.out.println("Supplier not found: " + supplierID);
        }
    }

    // Get supplier by ID
    public Supplier getSupplierById(int supplierID) {
        // Fetch supplier from the database through the DTO
        return supplierDTO.read(supplierID);
    }

    // Get all suppliers
    public List<Supplier> getSuppliers() {
        // Fetch all suppliers from the database through the DTO
        return supplierDTO.readAll();
    }

    // Delete a supplier from the database
    public void deleteSupplier(int supplierID) {
        Supplier supplierToDelete = getSupplierById(supplierID);
        if (supplierToDelete != null) {
//            Agreement agreement = supplierToDelete.getSupplierAgreement();
//            if (agreement != null) {
//                agreementDTO.delete(agreement.getAgreementID()); // Delete the agreement first
//            }
            supplierDTO.delete(supplierID); // Delete the supplier from the database
            System.out.println("Supplier deleted: " + supplierID);
        } else {
            System.out.println("Supplier not found: " + supplierID);
        }
    }

    // Method to update supplier in the database
    public void updateSupplier(Supplier supplier) {
        supplierDTO.update(supplier); // Update supplier details in the database
    }
}
