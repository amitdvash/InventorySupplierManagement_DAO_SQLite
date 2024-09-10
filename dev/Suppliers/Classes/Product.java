package dev.Suppliers.Classes;

import java.util.HashMap;

public class Product {
    private String productID;
    private String catalogID;
    private String name;
    private HashMap<Integer, Double> discountDetails;
    private double price;
    private int expirationDays;
    private double weight;
    private Agreement agreement;  // Add reference to Agreement

    public Product(String productID, String catalogID, String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement) {
        this.productID = productID;
        this.catalogID = catalogID;
        this.name = name;
        this.discountDetails = discountDetails;
        this.price = price;
        this.expirationDays = expirationDays;
        this.weight = weight;
        this.agreement = agreement;
    }

    // Getters and Setters
    public String getProductID() { return productID; }
    public String getCatalogID() { return catalogID; }
    public String getName() { return name; }
    public HashMap<Integer, Double> getDiscountDetails() { return discountDetails; }
    public void setDiscountDetails(HashMap<Integer, Double> discountDetails) {
        this.discountDetails = discountDetails;
        // Reflect the change in the Agreement
        if (agreement != null) {
            agreement.updateDiscountDetails(catalogID, discountDetails);  // Synchronize discount in Agreement
        }
    }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getExpirationDays() { return expirationDays; }
    public void setExpirationDays(int expirationDays) { this.expirationDays = expirationDays; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    // Link to Agreement
    public Agreement getAgreement() { return agreement; }
    public void setAgreement(Agreement agreement) { this.agreement = agreement; }
}
