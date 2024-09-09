package dev.Suppliers.Classes;
import java.util.Date;

public class Order {
    private String orderID;
    private String supplierID;
    private Date orderDate;

    public Order(String orderID, String supplierID, Date orderDate) {
        this.orderID = orderID;
        this.supplierID = supplierID;
        this.orderDate = orderDate;
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
}
