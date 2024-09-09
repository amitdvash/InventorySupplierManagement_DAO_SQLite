package dev.Suppliers.Classes;

import java.util.Map;

public class Product {
    private String productID;
    private String catalogID;
    private String name;
    private Map<Integer, Double> discountDetails;
    private double price;
    private int experationDate;
    private double weight;

    public Product(String productID, String catalogID, String name, Map<Integer, Double> discountDetails, double price, int experationDate, double weight) {
        this.productID = productID;
        this.catalogID = catalogID;
        this.name = name;
        this.discountDetails = discountDetails;
        this.price = price;
        this.experationDate = experationDate;
        this.weight = weight;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscountDetails(Map<Integer, Double> discountDetails) {
        this.discountDetails = discountDetails;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setExperationDate(int experationDate) {
        this.experationDate = experationDate;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getProductID() {
        return productID;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Double> getDiscountDetails() {
        return discountDetails;
    }

    public double getPrice() {
        return price;
    }

    public int getExperationDate() {
        return experationDate;
    }

    public double getWeight() {
        return weight;
    }
}
