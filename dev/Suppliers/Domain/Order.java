package dev.Suppliers.Domain;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order {
    private String orderID;
    private String supplierID;
    private Date orderDate;
    private List<Product> productList;
    private static int runningIndex = 1;

    public Order(String supplierID, Date orderDate, List<Product> productList) {
        this.orderID = "O" + runningIndex++;
        this.supplierID = supplierID;
        this.orderDate = orderDate;
        this.productList = productList;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public List<Product> getProductList() { // Getter for product list
        return productList;
    }

    // Method to add a product to the order
    public void addProduct(Product product) {
        if (!productList.contains(product)) {
            productList.add(product);
            System.out.println("Product added to the order.");
        } else {
            System.out.println("Product is already in the order.");
        }
    }

    // Method to remove a product from the order
    public void removeProduct(Product product) {
        if (productList.contains(product)) {
            productList.remove(product);
            System.out.println("Product removed from the order.");
        } else {
            System.out.println("Product not found in the order.");
        }
    }

    // Method to print the order details, including catalog numbers of products and supplier ID
    public void printOrderDetails() {
        System.out.println("Order ID: " + this.orderID);
        System.out.println("Supplier ID: " + this.supplierID);
        System.out.println("Products Ordered:");

        if (productList != null && !productList.isEmpty()) {
            for (Product product : productList) {
                System.out.println("  - Product Name: " + product.getName() + ", Catalog ID: " + product.getCatalogID() + ", Price: " + product.getPrice());

                // Print discount details for the product
                HashMap<Integer, Double> discounts = product.getDiscountDetails();
                if (discounts != null && !discounts.isEmpty()) {
                    System.out.println("    Discounts:");
                    for (Integer quantity : discounts.keySet()) {
                        Double discountPercent = discounts.get(quantity);
                        System.out.println("      - Buy " + quantity + " units or more: " + discountPercent + "% discount");
                    }
                } else {
                    System.out.println("    No discounts available.");
                }
            }
        } else {
            System.out.println("  No products in this order.");
        }
        System.out.println("---------------------------");
    }

}
