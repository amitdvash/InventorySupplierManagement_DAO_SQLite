package dev.Suppliers.Domain;

import java.util.HashMap;

public class Product {
    private int catalogID; // ID managed by the database, changed to int
    private String name;
    private HashMap<Integer, Double> discountDetails;
    private double price;
    private int expirationDays;
    private double weight;
    private Agreement agreement;

    // Constructor with catalogID
    public Product(int catalogID, String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement) {
        this.catalogID = catalogID; // ID set from the database or DTO
        this.name = name;
        this.discountDetails = discountDetails;
        this.price = price;
        this.expirationDays = expirationDays;
        this.weight = weight;
        this.agreement = agreement;
    }

    // Constructor for DTO creation without ID
    public Product(String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight) {
        this.name = name;
        this.discountDetails = discountDetails;
        this.price = price;
        this.expirationDays = expirationDays;
        this.weight = weight;
    }


    public Product(int catalogID, String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight) {
        this.catalogID = catalogID; // Integer catalogID
        this.name = name;
        this.discountDetails = discountDetails;
        this.price = price;
        this.expirationDays = expirationDays;
        this.weight = weight;
    }
    // Getters and Setters
    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, Double> getDiscountDetails() {
        return discountDetails;
    }

    public void setDiscountDetails(HashMap<Integer, Double> discountDetails) {
        this.discountDetails = discountDetails;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(int expirationDays) {
        this.expirationDays = expirationDays;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    // Print discount details for the product
    public void printDiscountDetails() {
        System.out.println("Discount Details for Product: " + name);
        if (discountDetails != null && !discountDetails.isEmpty()) {
            for (Integer quantity : discountDetails.keySet()) {
                Double discountPercent = discountDetails.get(quantity);
                System.out.println("  - Buy " + quantity + " units or more: " + discountPercent + "% discount");
            }
        } else {
            System.out.println("  No discounts available.");
        }
        System.out.println("-------------------------------");
    }
}
