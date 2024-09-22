package dev.Suppliers.Domain;

import java.util.*;

public class ProductController {
    // Collection to store product-supplier mapping
    private HashMap<String, List<Supplier>> productSupplierMap;

    // Existing createProduct method
    public Product createProduct(String name, HashMap<Integer, Double> discountDetails, double price, int expirationDays, double weight, Agreement agreement) {
        Product product = new Product(name, discountDetails, price, expirationDays, weight, agreement);
        System.out.println("Product created: " + product.getCatalogID());
        return product;
    }

    public ProductController() {
        productSupplierMap = new HashMap<>();
    }

    // Method to add a supplier for a product
    public void addSupplierForProduct(String productName, Supplier supplier) {
        productSupplierMap.computeIfAbsent(productName, k -> new ArrayList<>()).add(supplier);
    }

    // Method to find the cheapest supplier for a product and quantity
    public Supplier findCheapestSupplier(String productName, int quantity) {
        List<Supplier> suppliers = productSupplierMap.get(productName);

        if (suppliers == null || suppliers.isEmpty()) {
            System.out.println("No suppliers found for product: " + productName);
            return null;
        }

        Supplier cheapestSupplier = null;
        double lowestPrice = Double.MAX_VALUE;

        for (Supplier supplier : suppliers) {
            double price = calculateTotalPrice(supplier, productName, quantity);
            if (price < lowestPrice) {
                lowestPrice = price;
                cheapestSupplier = supplier;
            }
        }

        return cheapestSupplier;
    }

    // Helper method to calculate the total price based on supplier's discount details
    private double calculateTotalPrice(Supplier supplier, String productName, int quantity) {
        Product product = supplier.getSupplierAgreement().getProductByName(productName);
        if (product == null) {
            return Double.MAX_VALUE;
        }

        double basePrice = product.getPrice() * quantity;
        double discount = getDiscountForQuantity(product.getDiscountDetails(), quantity);

        return basePrice - discount;
    }

    // Helper method to get the discount for a specific quantity from discount details
    private double getDiscountForQuantity(HashMap<Integer, Double> discountDetails, int quantity) {
        double discount = 0.0;

        for (Map.Entry<Integer, Double> entry : discountDetails.entrySet()) {
            if (quantity >= entry.getKey()) {
                discount = entry.getValue();
            }
        }

        return discount;
    }

    public List<String> getAllProductNames() {
        return productSupplierMap.keySet().stream().toList();
    }

    // New method to remove a supplier from a product
    public void removeSupplierFromProduct(String productName, Supplier supplier) {
        List<Supplier> suppliers = productSupplierMap.get(productName);

        if (suppliers != null) {
            suppliers.remove(supplier);
            if (suppliers.isEmpty()) {
                productSupplierMap.remove(productName);
                System.out.println("Product " + productName + " removed from the system as no suppliers are left.");
            } else {
                System.out.println("Supplier " + supplier.getSupplierID() + " removed from product " + productName);
            }
        } else {
            System.out.println("No such product or supplier exists in the system.");
        }
    }

    public HashMap<String, List<Supplier>> getProductSupplierMap() {
        return productSupplierMap;
    }
}
