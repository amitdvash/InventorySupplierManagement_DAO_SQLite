package dev.Suppliers.Domain;

import java.util.HashMap;

public class Product {
    private static int runningIndex = 1;  // Static running index
    private String catalogID;
    private String name;
    private HashMap<Integer, Double> discountDetails;
    private double price;
    private int expirationDays;
    private double weight;
    private Agreement agreement;

    // Constructor - ID is auto-assigned
    public Product(String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement) {
        this.catalogID =  "P" + runningIndex++;
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
    }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getExpirationDays() { return expirationDays; }
    public void setExpirationDays(int expirationDays) { this.expirationDays = expirationDays; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Agreement getAgreement() { return agreement; }
    public void setAgreement(Agreement agreement) { this.agreement = agreement; }
}
