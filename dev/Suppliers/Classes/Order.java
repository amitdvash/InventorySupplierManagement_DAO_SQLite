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

    @Override
    public String toString() {
        return "Order{" +
                ", orderDate=" + orderDate +
                "orderID='" + orderID + '\'' +
                ", supplierID='" + supplierID + '\'' +
                '}';
    }

}
