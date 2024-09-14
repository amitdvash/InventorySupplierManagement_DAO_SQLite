package dev.Suppliers.Test;

import dev.Suppliers.Domain.Order;
import dev.Suppliers.Domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class OrderTest {
    private Order order;

    @BeforeEach
    void setUp() {
        HashMap<Product, Integer> productMap = new HashMap<>();
        productMap.put(new Product("Banana", new HashMap<>(), 10.0, 7, 1.5, null), 5);
        order = new Order("S1", new Date(), productMap);  // Updated constructor to take product map
    }

    @Test
    void testGetOrderID() {
        assertNotNull(order.getOrderID());
    }

    @Test
    void testGetSupplierID() {
        assertEquals("S1", order.getSupplierID());
    }

    @Test
    void testGetOrderDate() {
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testGetProductList() {
        assertFalse(order.getProductQuantityMap().isEmpty());
    }

    @Test
    void testOrderTotalBeforeDiscount() {
        double total = order.getPriceBeforeDiscount();
        assertEquals(50.0, total);  // 5 units of Banana at 10.0 each
    }


}
