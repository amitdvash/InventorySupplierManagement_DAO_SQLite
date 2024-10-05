package dev.Suppliers.Domain;

import dev.Suppliers.DataBase.OrderDTO;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderController {
    private OrderDTO orderDTO;

    public OrderController(Connection connection) {
        orderDTO = new OrderDTO(connection);
    }

    // Method to create a new order
    public int createOrder(Supplier supplier, HashMap<Product, Integer> productQuantityMap, boolean isConstantDelivery) {
        Order newOrder = new Order(supplier, productQuantityMap, isConstantDelivery);
        int orderID = orderDTO.create(newOrder); // Save to database
        System.out.println("Order created: " + newOrder.getOrderID());
        return orderID;
    }

    // Method to update an order
    public boolean updateOrder(int orderID, HashMap<Product, Integer> newProductQuantityMap) {
        Order order = orderDTO.read(orderID);
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
        orderDTO.update(order); // Update in database
        System.out.println("Order updated: " + orderID);
        return true;
    }

    // Method to turn a constant order into a regular one
    public boolean turnConstantOrderToRegular(int orderID) {
        Order order = orderDTO.read(orderID);
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
        order.updateDeliveryDate();
        orderDTO.update(order); // Update in database
        System.out.println("Order " + orderID + " has been changed to a regular order with the next delivery date.");
        return true;
    }

    // Method to generate a report of all orders
    public void generateOrdersReport() {
        List<Order> orders = orderDTO.readAll();
        if (orders.isEmpty()) {
            System.out.println("No existing orders in the system.");
            return;
        }

        System.out.println("----- Orders Report -----");
        for (Order order : orders) {
            order.printOrderDetails();  // Use the printOrderDetails() method
        }
    }

    // Method to find an order by ID
    public Order findOrderByID(int orderID) {
        return orderDTO.read(orderID);
    }

    // Method to get all active orders
    public List<Order> getActiveOrders() {
        List<Order> activeOrders = new ArrayList<>();
        for (Order order : orderDTO.readAll()) {
            if (order.isOrderActive()) {
                activeOrders.add(order);
            }
        }
        return activeOrders;
    }

    // Method to insert product details into OrdersOnTheWay table
    public void insertOrderOnTheWay(int orderID, int catalogID, int quantity) {
        Date deliveryDate = new java.sql.Date(System.currentTimeMillis());
        orderDTO.insertOrderOnTheWay(orderID, catalogID, quantity, deliveryDate);
    }
}
