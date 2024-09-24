package dev.Suppliers.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController {

    private HashMap<String, Order> orders; // Use HashMap to store orders by orderID

    public OrderController() {
        orders = new HashMap<>();
    }

    // Method to create a new order without orderDate parameter
    public void createOrder(Supplier supplier, HashMap<Product, Integer> productQuantityMap, boolean isConstantDelivery) {
        Order newOrder = new Order(supplier, productQuantityMap, isConstantDelivery); // Order will set the current date internally
        orders.put(newOrder.getOrderID(), newOrder);
        System.out.println("Order created: " + newOrder.getOrderID());
    }

    // Method to update an order
    public boolean updateOrder(String orderID, HashMap<Product, Integer> newProductQuantityMap) {
        Order order = orders.get(orderID);
        if (order == null) {
            System.out.println("Order not found: " + orderID);
            return false;
        }

        if (!order.isOrderActive()) {
            System.out.println("Order " + orderID + " cannot be updated as it is no longer active.");
            return false;
        }

        // Update product quantities and recalculate the order summary
        order.setProductQuantityMap(newProductQuantityMap);
        order.calculateOrderSummary();
        System.out.println("Order updated: " + orderID);
        return true;
    }

    // Method to turn a constant order into a regular one
    public boolean turnConstantOrderToRegular(String orderID) {
        Order order = orders.get(orderID);
        if (order == null) {
            System.out.println("Order not found: " + orderID);
            return false;
        }

        if (!order.isConstantDelivery()) {
            System.out.println("Order " + orderID + " is already a regular order.");
            return false;
        }

        // Update the order to a regular order and set the last delivery date
        order.setConstantDelivery(false);
        order.updateDeliveryDate(); // Update delivery date using getClosestSupplyDay method in Order class
        System.out.println("Order " + orderID + " has been changed to a regular order with the next delivery date.");
        return true;
    }

    // Method to generate a report of all orders
    public void generateOrdersReport() {
        if (orders.isEmpty()) {
            System.out.println("No existing orders in the system.");
            return;
        }

        System.out.println("----- Orders Report -----");
        for (Order order : orders.values()) {
            order.printOrderDetails();  // Use the printOrderDetails() method
        }
    }

    // Method to find an order by ID
    public Order findOrderByID(String orderID) {
        return orders.get(orderID);
    }

    // Method to get all active orders
    public List<Order> getActiveOrders() {
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.isOrderActive()) {
                activeOrders.add(order);
            }
        }
        return activeOrders;
    }


}
