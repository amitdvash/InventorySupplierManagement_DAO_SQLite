package dev.Inventory.Classes;

import java.time.LocalDate;

public class Discount {
    private int id;  // Database primary key
    private double discountRate;  // Discount rate as a percentage (e.g., 10 for 10%)
    private LocalDate startDate;  // Start date of the discount
    private LocalDate endDate;  // End date of the discount

    // Constructor for creating a new discount
    public Discount(double discountRate, LocalDate startDate, LocalDate endDate) {
        this.setDiscountRate(discountRate);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    // Constructor for loading a discount from the database with an existing ID
    public Discount(int id, double discountRate, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.setDiscountRate(discountRate);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        if (discountRate < 0 || discountRate > 100) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
        }
        this.discountRate = discountRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }

    // Method to check if the discount is currently active
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    // Helper method to determine if a discount is expired
    public boolean isExpired() {
        return LocalDate.now().isAfter(endDate);
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", discountRate=" + discountRate +
                "%, startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
