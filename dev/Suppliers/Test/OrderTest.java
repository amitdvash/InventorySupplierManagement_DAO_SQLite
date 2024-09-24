

import dev.Suppliers.Domain.Order;
import dev.Suppliers.Domain.Product;
import dev.Suppliers.Domain.Supplier;
import dev.Suppliers.Enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class OrderTest {
    private Order order;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        // Create a Supplier object
        supplier = new Supplier("12345", "987654321", PaymentMethod.CreditCard, null, null);

        // Create a Product and add to the map
        HashMap<Product, Integer> productMap = new HashMap<>();
        productMap.put(new Product("Banana", new HashMap<>(), 10.0, 7, 1.5, null), 5);

        // Updated constructor to take Supplier and product map
        order = new Order(supplier, productMap, false);
    }

    @Test
    void testGetOrderID() {
        assertNotNull(order.getOrderID());
    }

    @Test
    void testGetSupplier() {
        assertEquals(supplier, order.getSupplier());
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
