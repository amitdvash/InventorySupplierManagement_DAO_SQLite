package dev.Suppliers.Domain;

import java.util.HashMap;
import java.util.List;

public class Agreement {
    private static int runningIndex = 1;  // Static running index
    private int agreementID;
    private Supplier supplier;
    private List<Product> productList;
    private HashMap<String, HashMap<Integer, Double>> discountDetails;
    private List<String> supplyDays;
    private boolean selfSupply;

    // Constructor - ID is auto-assigned
    public Agreement(List<Product> productList, HashMap<String, HashMap<Integer, Double>> discountDetails,
                     List<String> supplyDays, boolean selfSupply) {
        this.agreementID = runningIndex++;  // Auto-generate agreementID
        this.productList = productList;
        this.discountDetails = discountDetails;
        this.supplyDays = supplyDays;
        this.selfSupply = selfSupply;
    }

    // Getters and Setters
    public int getAgreementID() {
        return agreementID;
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


    public HashMap<String, HashMap<Integer, Double>> getDiscountDetails() {
        return discountDetails;
    }



    public void setDiscountDetails(HashMap<String, HashMap<Integer, Double>> discountDetails) {
        this.discountDetails = discountDetails;
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

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    public void printAgreementDetails() {
        System.out.println("----- Agreement Details -----");
        System.out.println("Agreement ID: " + agreementID);

        System.out.println("Products covered by this agreement:");
        if (productList != null && !productList.isEmpty()) {
            for (Product product : productList) {
                System.out.println("  - Product Name: " + product.getName() + ", Catalog ID: " + product.getCatalogID() + ", Price: " + product.getPrice());

                // Print discount details for the product

                HashMap<Integer, Double> discounts = product.getDiscountDetails();
                if (discounts != null && !discounts.isEmpty()) {
                    System.out.println("    Discounts:");
                    for (Integer quantity : discounts.keySet()) {
                        Double discountPercent = discounts.get(quantity);
                        System.out.println("      - Buy " + quantity + " units or more: " + discountPercent + "% discount");
                    }
                } else {
                    System.out.println("    No discounts available.");
                }
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

    // Print only discount details
    public void printDiscountDetails() {
        System.out.println("----- Discount Details -----");
        if (discountDetails != null && !discountDetails.isEmpty()) {
            for (String productID : discountDetails.keySet()) {
                HashMap<Integer, Double> discounts = discountDetails.get(productID);
                System.out.println("Product ID: " + productID);
                if (discounts != null && !discounts.isEmpty()) {
                    for (Integer quantity : discounts.keySet()) {
                        Double discountPercent = discounts.get(quantity);
                        System.out.println("  - Buy " + quantity + " units or more: " + discountPercent + "% discount");
                    }
                } else {
                    System.out.println("  No discounts available.");
                }
            }
        } else {
            System.out.println("No discount details available.");
        }
        System.out.println("------------------------------");
    }

}
