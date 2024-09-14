package dev.Suppliers.Domain;

import java.util.Date;
import java.util.HashMap;

public class Order {
    private String orderID;
    private String supplierID;
    private Date orderDate;
    private HashMap<Product, Integer> productQuantityMap;  // Key: Product, Value: Quantity
    private double priceBeforeDiscount;
    private double discountAmount;
    private double priceAfterDiscount;
    private static int runningIndex = 1;

    public Order(String supplierID, Date orderDate, HashMap<Product, Integer> productQuantityMap) {
        this.orderID = "O" + runningIndex++;
        this.supplierID = supplierID;
        this.orderDate = orderDate;
        this.productQuantityMap = productQuantityMap;
        calculateOrderSummary();  // Calculate price and discounts during initialization
    }

    // Calculate priceBeforeDiscount, discountAmount, and priceAfterDiscount
    private void calculateOrderSummary() {
        priceBeforeDiscount = 0;
        discountAmount = 0;

        for (Product product : productQuantityMap.keySet()) {
            int quantity = productQuantityMap.get(product);
            double productPrice = product.getPrice() * quantity;
            priceBeforeDiscount += productPrice;

            // Calculate the highest applicable discount for the product
            double productDiscount = calculateHighestDiscount(product, quantity);
            discountAmount += productDiscount * productPrice / 100;  // Convert to discount in price terms
        }

        priceAfterDiscount = priceBeforeDiscount - discountAmount;
    }

    // Helper method to calculate the highest applicable discount for a product based on quantity
    private double calculateHighestDiscount(Product product, int quantity) {
        double highestDiscount = 0;
        HashMap<Integer, Double> discountDetails = product.getDiscountDetails();

        if (discountDetails != null) {
            for (Integer minQuantity : discountDetails.keySet()) {
                if (quantity >= minQuantity) {
                    highestDiscount = Math.max(highestDiscount, discountDetails.get(minQuantity));
                }
            }
        }

        return highestDiscount;  // Return the highest discount applicable for the given quantity
    }

    // Getters
    public String getOrderID() {
        return orderID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public HashMap<Product, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    public double getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    // Method to print the order details, including catalog numbers of products and supplier ID
    public void printOrderDetails() {
        System.out.println("Order ID: " + this.orderID);
        System.out.println("Supplier ID: " + this.supplierID);
        System.out.println("Order Date: " + this.orderDate);
        System.out.println("Products Ordered:");

        if (productQuantityMap != null && !productQuantityMap.isEmpty()) {
            for (Product product : productQuantityMap.keySet()) {
                int quantity = productQuantityMap.get(product);
                System.out.println("  - Product Name: " + product.getName() + ", Catalog ID: " + product.getCatalogID() +
                        ", Price: " + product.getPrice() + ", Quantity: " + quantity);

                // Print discount details for the product
                HashMap<Integer, Double> discounts = product.getDiscountDetails();
                if (discounts != null && !discounts.isEmpty()) {
                    System.out.println("    Discounts:");
                    for (Integer minQuantity : discounts.keySet()) {
                        Double discountPercent = discounts.get(minQuantity);
                        System.out.println("      - Buy " + minQuantity + " units or more: " + discountPercent + "% discount");
                    }
                } else {
                    System.out.println("    No discounts available.");
                }
            }
        } else {
            System.out.println("  No products in this order.");
        }

        // Print the summary of the order
        System.out.println("Total Price Before Discount: " + priceBeforeDiscount);
        System.out.println("Total Discount: " + discountAmount);
        System.out.println("Total Price After Discount: " + priceAfterDiscount);
        System.out.println("---------------------------");
    }
}
