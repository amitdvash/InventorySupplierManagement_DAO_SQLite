package dev.Suppliers.Classes;
import java.util.Date;
import java.util.List;

public class Order {
    private String orderID;
    private String supplierID;
    private Date orderDate;
    private List<Product> productList;

    public Order(String orderID, String supplierID, Date orderDate, List<Product> productList) {
        this.orderID = orderID;
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
        System.out.println("Order ID: " + orderID);
        System.out.println("Supplier ID: " + supplierID);
        System.out.println("Order Date: " + orderDate);
        if (productList != null) {
            for (Product product : productList) {
                System.out.println("    Product Name: " + product.getName());
                System.out.println("    Catalog ID: " + product.getCatalogID());
                System.out.println("    Price: " + product.getPrice());
                System.out.println("    Discount: " + product.getDiscountDetails());
            }
        } else {
            System.out.println("    No products in this order.");
        }
        System.out.println("---------------------------");
    }


}
