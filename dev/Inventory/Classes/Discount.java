
package dev.Inventory.Classes;

import java.time.LocalDate;

public class Discount {
    private int id;  // Database primary key
    private double discountRate;  // Discount rate as a percentage (e.g., 10 for 10%)
    private LocalDate startDate;  // Start date of the discount
    private LocalDate endDate;  // End date of the discount\

    // Constructor for creating a new discount
    public Discount(double discountRate, LocalDate startDate, LocalDate endDate ) {
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
            System.out.println("Discount rate must be between 0 and 100");
            return;
        }
        this.discountRate = discountRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            System.out.println("Error: Start date cannot be null.");
            return;  // Exit the method if start date is null
        }
        if (endDate != null && startDate.isAfter(endDate)) {
            System.out.println("Error: Start date cannot be after end date.");
            return;  // Exit the method if start date is after the end date
        }
        this.startDate = startDate;  // Set the start date only if it's valid
        System.out.println("Start date set to: " + startDate);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            System.out.println("Error: End date cannot be null.");
            return;  // Exit the method if end date is null
        }
        if (startDate != null && endDate.isBefore(startDate)) {
            System.out.println("Error: End date cannot be before start date.");
            return;  // Exit the method if end date is before the start date
        }
        this.endDate = endDate;  // Set the end date only if it's valid
        System.out.println("End date set to: " + endDate);
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
