package dev.Suppliers.DataBase;

import dev.Suppliers.Domain.Order;
import dev.Suppliers.Domain.Product;
import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Interfaces.IDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDTO implements IDTO<Order> {
    private Connection connection;
    private SupplierDTO supplierDTO;
    private ProductDTO productDTO; // Added ProductDTO for product-related operations

    public OrderDTO(Connection connection) {
        this.connection = connection;
        this.supplierDTO = new SupplierDTO(connection);
        this.productDTO = new ProductDTO(connection); // Initialize ProductDTO
    }

    @Override
    public int create(Order order) {
        String sql = "INSERT INTO Orders (supplierID, orderDate, deliveryDate, deliveryDay, priceBeforeDiscount, discountAmount, priceAfterDiscount, isConstantDelivery) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getSupplier().getSupplierID()); // Set supplierID from the Supplier object
            pstmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime())); // Set orderDate
            pstmt.setDate(3, new java.sql.Date(order.getDeliveryDate().getTime())); // Set deliveryDate
            pstmt.setString(4, order.getDeliveryDay()); // Set deliveryDay
            pstmt.setDouble(5, order.getPriceBeforeDiscount()); // Set priceBeforeDiscount
            pstmt.setDouble(6, order.getDiscountAmount()); // Set discountAmount
            pstmt.setDouble(7, order.getPriceAfterDiscount()); // Set priceAfterDiscount
            pstmt.setBoolean(8, order.isConstantDelivery()); // Set isConstantDelivery

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int orderID = rs.getInt(1); // Get generated orderID
                order.setOrderID(orderID); // Set orderID in the Order object
                insertOrderProducts(order); // Insert products into orderProducts table
                return orderID; // Return the generated orderID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if creation failed
    }


    @Override
    public List<Order> readAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = supplierDTO.read(rs.getInt("supplierID"));
                HashMap<Product, Integer> productQuantityMap = getProductsForOrder(rs.getInt("orderID"));
                boolean isConstantDelivery = rs.getBoolean("isConstantDelivery");

                Order order = new Order(supplier, productQuantityMap, isConstantDelivery);
                order.setOrderID(rs.getInt("orderID"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setDeliveryDate(rs.getDate("deliveryDate"));
                order.setDeliveryDay(rs.getString("deliveryDay"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE Orders SET supplierID = ?, orderDate = ?, deliveryDate = ?, deliveryDay = ?, priceBeforeDiscount = ?, discountAmount = ?, priceAfterDiscount = ?, isConstantDelivery = ? WHERE orderID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order.getSupplier().getSupplierID());
            pstmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(order.getDeliveryDate().getTime()));
            pstmt.setString(4, order.getDeliveryDay());
            pstmt.setDouble(5, order.getPriceBeforeDiscount());
            pstmt.setDouble(6, order.getDiscountAmount());
            pstmt.setDouble(7, order.getPriceAfterDiscount());
            pstmt.setBoolean(8, order.isConstantDelivery());
            pstmt.setInt(9, order.getOrderID());
            pstmt.executeUpdate();

            // Update products in orderProducts table
            deleteOrderProducts(order);
            insertOrderProducts(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Orders WHERE orderID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order read(int orderID) {
        Order order = null;
        String sql = "SELECT * FROM Orders WHERE orderID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Supplier supplier = supplierDTO.read(rs.getInt("supplierID"));
                HashMap<Product, Integer> productQuantityMap = getProductsForOrder(orderID);
                boolean isConstantDelivery = rs.getBoolean("isConstantDelivery");

                order = new Order(supplier, productQuantityMap, isConstantDelivery);
                order.setOrderID(rs.getInt("orderID"));
                order.setOrderDate(rs.getDate("orderDate"));
                order.setDeliveryDate(rs.getDate("deliveryDate"));
                order.setDeliveryDay(rs.getString("deliveryDay"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public void insertOrderProducts(Order order) {
        String sql = "INSERT INTO orderProducts (orderID, catalogID, quantity, price, discount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Map.Entry<Product, Integer> entry : order.getProductQuantityMap().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double price = product.getPrice() * quantity; // Calculate the price based on quantity

                // Get the discount for the product's catalogID from the Order's getDiscountDetails method
                double discount = order.getDiscountDetails(product.getCatalogID()).getOrDefault(quantity, 0.0);

                pstmt.setInt(1, order.getOrderID());
                pstmt.setInt(2, product.getCatalogID());
                pstmt.setInt(3, quantity);
                pstmt.setDouble(4, price);
                pstmt.setDouble(5, discount);
                pstmt.addBatch();
            }
            pstmt.executeBatch(); // Execute all the batches together
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrderProducts(Order order) {
        String sql = "DELETE FROM orderProducts WHERE orderID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, order.getOrderID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Product, Integer> getProductsForOrder(int orderID) {
        HashMap<Product, Integer> productQuantityMap = new HashMap<>();
        String sql = "SELECT p.catalogID, p.name, p.price, p.expirationDays, p.weight, op.quantity FROM orderProducts op JOIN Products p ON op.catalogID = p.catalogID WHERE op.orderID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("catalogID"),
                        rs.getString("name"),
                        getProductDiscountDetails(rs.getInt("catalogID")),
                        rs.getDouble("price"),
                        rs.getInt("expirationDays"),
                        rs.getDouble("weight")
                );
                productQuantityMap.put(product, rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productQuantityMap;
    }

    private HashMap<Integer, Double> getProductDiscountDetails(int catalogID) {
        HashMap<Integer, Double> discountDetails = new HashMap<>();
        String sql = "SELECT * FROM productDiscounts WHERE catalogID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catalogID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                discountDetails.put(rs.getInt("quantity"), rs.getDouble("discount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discountDetails;
    }
}
