package dev.Inventory.Classes;

import dev.Inventory.Enums.E_Item_Place;
import dev.Inventory.Enums.E_Item_Status;

import java.time.LocalDate;

public class Item {
    private int id;  // This will correspond to the auto-incremented ID in the database
    private String name;
    private double costPrice;
    private double sellingPrice;
    private double priceAfterDiscount;  // For discount management
    private String manufacturer;
    private String category;
    private String subCategory;
    private double size;
    private LocalDate expiryDate;  // Optional, can be null
    private E_Item_Status status;
    private E_Item_Place place;

    // Constructor for creating a new item
    public Item(String name, double costPrice, double sellingPrice, String manufacturer,
                String category, String subCategory, double size, LocalDate expiryDate,
                E_Item_Status status, E_Item_Place place) {
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.priceAfterDiscount = sellingPrice;  // Initially, no discount applied
        this.manufacturer = manufacturer;
        this.category = category;
        this.subCategory = subCategory;
        this.size = size;
        this.expiryDate = expiryDate;
        this.status = status;
        this.place = place;
    }

    // Constructor for loading an item from the database with an existing ID
    public Item(int id, String name, double costPrice, double sellingPrice, String manufacturer,
                String category, String subCategory, double size, LocalDate expiryDate,
                E_Item_Status status, E_Item_Place place) {
        this.id = id;
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.priceAfterDiscount = sellingPrice;  // Initially, no discount applied
        this.manufacturer = manufacturer;
        this.category = category;
        this.subCategory = subCategory;
        this.size = size;
        this.expiryDate = expiryDate;
        this.status = status;
        this.place = place;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;  // The database will provide this value when inserted
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void applyDiscount(double discountRate) {
        this.priceAfterDiscount = sellingPrice - (sellingPrice * discountRate / 100);
    }

    public void cancelDiscount() {
        this.priceAfterDiscount = sellingPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public E_Item_Status getStatus() {
        return status;
    }

    public void setStatus(E_Item_Status status) {
        this.status = status;
    }

    public E_Item_Place getPlace() {
        return place;
    }

    public void setPlace(E_Item_Place place) {
        this.place = place;
    }

    // Update status based on expiry
    public void updateStatus() {
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.status = E_Item_Status.EXPIRED;
        } else if (expiryDate != null && expiryDate.isBefore(LocalDate.now().plusDays(7))) {
            this.status = E_Item_Status.about_to_expire;
        }
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    // i need b\\
    //exipert


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", costPrice=" + costPrice +
                ", sellingPrice=" + sellingPrice +
                ", priceAfterDiscount=" + priceAfterDiscount +
                ", manufacturer='" + manufacturer + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", size=" + size +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                ", place=" + place +
                '}';
    }
}


