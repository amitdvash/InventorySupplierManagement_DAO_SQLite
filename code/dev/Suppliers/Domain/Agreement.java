package dev.Suppliers.Domain;

import java.util.HashMap;
import java.util.List;

public class Agreement {
    private int agreementID; // ID managed by the database
    private int supplierID; // Reference to the supplier
    private List<Product> productList;
    private List<String> supplyDays;
    private boolean selfSupply;

    // Constructor without auto-generated ID
    public Agreement(int agreementID, int supplierID, List<Product> productList, List<String> supplyDays, boolean selfSupply) {
        this.agreementID = agreementID; // ID set from the database or DTO
        this.supplierID = supplierID;
        this.productList = productList;
        this.supplyDays = supplyDays;
        this.selfSupply = selfSupply;
    }

    // Simplified constructor for DTO creation without ID
    public Agreement(int supplierID, List<Product> productList, List<String> supplyDays, boolean selfSupply) {
        this.supplierID = supplierID;
        this.productList = productList;
        this.supplyDays = supplyDays;
        this.selfSupply = selfSupply;
    }

    // Getters and Setters
    public int getAgreementID() {
        return agreementID;
    }

    public void setAgreementID(int agreementID) {
        this.agreementID = agreementID;
    }


    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void addProduct(Product product){
        productList.add(product);
    }

    public void removeProduct(Product product) {
        if (productList.contains(product)) {
            productList.remove(product);
        } else {
            System.out.println("Product not found in the list.");
        }
    }

    public List<String> getSupplyDays() {
        return supplyDays;
    }

    public void setSupplyDays(List<String> supplyDays) {
        this.supplyDays = supplyDays;
    }

    public boolean isSelfSupply() {
        return selfSupply;
    }

    public void setSelfSupply(boolean selfSupply) {
        this.selfSupply = selfSupply;
    }

    // Method to get a product by its name from the productList
    public Product getProductByName(String productName) {
        for (Product product : productList) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        System.out.println("Product " + productName + " not found in this agreement.");
        return null;
    }

    public void printAgreementDetails() {
        System.out.println("----- Agreement Details -----");
        System.out.println("Agreement ID: " + agreementID);
        System.out.println("Supplier ID: " + supplierID);

        System.out.println("Products covered by this agreement:");
        if (productList != null && !productList.isEmpty()) {
            for (Product product : productList) {
                System.out.println("  - Product Name: " + product.getName() + ", Catalog ID: " + product.getCatalogID() + ", Price: " + product.getPrice());
                product.printDiscountDetails(); // Print discount details from Product
            }
        } else {
            System.out.println("  No products in this agreement.");
        }

        System.out.println("Supply Days:");
        if (supplyDays != null && !supplyDays.isEmpty()) {
            System.out.println("  " + String.join(", ", supplyDays));
        } else {
            System.out.println("  No specific supply days.");
        }

        System.out.println("Supplier Responsible for Supply: " + (selfSupply ? "Yes" : "No"));
        System.out.println("-------------------------------");
    }
}
