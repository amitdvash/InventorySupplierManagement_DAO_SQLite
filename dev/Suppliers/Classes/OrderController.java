package dev.Suppliers.Classes;

import java.util.ArrayList;
import java.util.List;

public class OrderController {
    private List<Order> orders = new ArrayList<>();
    private List<Supplier> suppliers = new ArrayList<>();  // Assuming we keep suppliers for reference

    // Add a new order
    public void addOrder(Order order) {
        orders.add(order);
        System.out.println("Order added: " + order.getOrderID());
    }

    // Get an order by ID
    public Order getOrder(String orderID) {
        for (Order order : orders) {
            if (order.getOrderID().equals(orderID)) {
                return order;
            }
        }
        System.out.println("Order not found: " + orderID);
        return null;
    }

    // Generate report for all orders
    public void generateOrdersReport() {
        System.out.println("----- Orders Report -----");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderID());
            System.out.println("Supplier ID: " + order.getSupplierID());

            List<Product> products = order.getProductList();
            if (products != null) {
                for (Product product : products) {
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

    // Assuming the suppliers list is maintained here for report purposes
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public Supplier getSupplier(String supplierID) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID)) {
                return supplier;
            }
        }
        System.out.println("Supplier not found: " + supplierID);
        return null;
    }
}
