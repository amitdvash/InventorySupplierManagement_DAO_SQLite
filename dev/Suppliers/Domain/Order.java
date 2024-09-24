package dev.Suppliers.Domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order {

    private String orderID;
    private Supplier supplier;
    private Date orderDate;
    private Date deliveryDate;
    private String deliveryDay; // New field to store the day of the week for delivery
    private HashMap<Product, Integer> productQuantityMap;  // Key: Product, Value: Quantity
    private double priceBeforeDiscount;
    private double discountAmount;
    private double priceAfterDiscount;
    private boolean isConstantDelivery; // New field to indicate if it's a recurring order
    private static int runningIndex = 1;

    public Order(Supplier supplier, HashMap<Product, Integer> productQuantityMap, boolean isConstantDelivery) {
        this.orderID = "O" + runningIndex++;
        this.supplier = supplier;
        this.orderDate = new java.util.Date();
        this.productQuantityMap = productQuantityMap;
        this.isConstantDelivery = isConstantDelivery;
        calculateOrderSummary();  // Calculate price and discounts during initialization
        setDeliveryDateAndDay();  // Set delivery date and day based on order type and supplier's agreement
    }

    public void calculateOrderSummary() {
        priceBeforeDiscount = 0;
        discountAmount = 0;

        for (Product product : productQuantityMap.keySet()) {
            int quantity = productQuantityMap.get(product);
            priceBeforeDiscount += product.getPrice() * quantity;

            // Calculate discount amount based on percentage
            double discountPercentage = calculateHighestDiscount(product, quantity);
            discountAmount += (product.getPrice() * quantity) * (discountPercentage / 100);
        }

        priceAfterDiscount = priceBeforeDiscount - discountAmount;
    }

    // Set delivery date and day based on the order type and supplier's agreement
    private void setDeliveryDateAndDay() {
        Agreement agreement = supplier.getSupplierAgreement();

        if (agreement == null || !agreement.isSelfSupply() || agreement.getSupplyDays() == null) {
            // If supermarket delivers or no specific supply days, set delivery day a week from order date
            this.deliveryDate = new Date(orderDate.getTime() + (7 * 24 * 60 * 60 * 1000));
            this.deliveryDay = getDayOfWeek(deliveryDate);
        } else {
            // Supplier self-supplies and has specific supply days
            this.deliveryDate = getClosestSupplyDay(orderDate, agreement.getSupplyDays());
            this.deliveryDay = getDayOfWeek(deliveryDate);
        }
    }

    // Helper method to calculate the closest supply day Date
    private Date getClosestSupplyDay(Date fromDate, List<String> supplyDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int closestDaysDifference = Integer.MAX_VALUE;
        Date closestDate = null;

        for (String supplyDay : supplyDays) {
            int targetDay = getDayOfWeekNumber(supplyDay);
            int daysDifference = (targetDay - currentDay + 7) % 7;
            daysDifference = daysDifference == 0 ? 7 : daysDifference; // Avoid same day

            if (daysDifference < closestDaysDifference) {
                closestDaysDifference = daysDifference;
                closestDate = new Date(fromDate.getTime() + (daysDifference * 24 * 60 * 60 * 1000));
            }
        }
        return closestDate;
    }

    // Helper method to get day of week from a Date object
    private String getDayOfWeek(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        return dateFormat.format(date);
    }

    // Helper method to convert day names to Calendar day numbers
    private int getDayOfWeekNumber(String day) {
        switch (day.toLowerCase()) {
            case "sunday": return Calendar.SUNDAY;
            case "monday": return Calendar.MONDAY;
            case "tuesday": return Calendar.TUESDAY;
            case "wednesday": return Calendar.WEDNESDAY;
            case "thursday": return Calendar.THURSDAY;
            case "friday": return Calendar.FRIDAY;
            case "saturday": return Calendar.SATURDAY;
            default: throw new IllegalArgumentException("Invalid day: " + day);
        }
    }

    // Helper method to calculate discount based on quantity
    private double calculateHighestDiscount(Product product, int quantity) {
        HashMap<Integer, Double> discountDetails = product.getDiscountDetails();
        double discount = 0.0;

        for (Integer minQuantity : discountDetails.keySet()) {
            if (quantity >= minQuantity) {
                discount = discountDetails.get(minQuantity);
            }
        }

        return discount;
    }

    // Check if the order is relevant based on delivery date
    public boolean isOrderActive() {
        if (isConstantDelivery()) {
            return true; // Constant deliveries are always active
        }

        Date today = new Date();
        return !deliveryDate.before(today); // Non-constant deliveries check the delivery date
    }

    public void updateDeliveryDate() {
        // Use the getClosestSupplyDay method to update the delivery date
        this.deliveryDate = getClosestSupplyDay(new Date(), supplier.getSupplierAgreement().getSupplyDays());
    }

    // Getters
    public String getOrderID() {
        return orderID;
    }

    public Supplier getSupplier() {
        return supplier;
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryDay() {
        return deliveryDay;
    }

    public boolean isConstantDelivery() {
        return isConstantDelivery;
    }

    public void setConstantDelivery(boolean constantDelivery) {
        isConstantDelivery = constantDelivery;
    }

    public void setProductQuantityMap(HashMap<Product, Integer> productQuantityMap) {
        this.productQuantityMap = productQuantityMap;
    }

    // Method to print the order details, including catalog numbers of products and supplier ID
    public void printOrderDetails() {
        System.out.println("Order ID: " + this.orderID);
        System.out.println("Supplier ID: " + this.supplier.getSupplierID());
        System.out.println("Constant Order: " + this.isConstantDelivery);
        System.out.println("Order Date: " + this.orderDate);
        System.out.println("Delivery Date: " + this.deliveryDate);
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
