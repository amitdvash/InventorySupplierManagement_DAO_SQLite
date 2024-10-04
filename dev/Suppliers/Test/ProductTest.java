//
//import dev.Suppliers.Domain.Agreement;
//import dev.Suppliers.Domain.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//
//public class ProductTest {
//    private Product product;
//
//    @BeforeEach
//    void setUp() {
//        Agreement agreement = new Agreement(new ArrayList<>(), new HashMap<>(), Arrays.asList("Monday"), true);  // Passing agreement
//        product = new Product( "Banana", new HashMap<>(), 10.0, 7, 1.5, agreement);  // Updated constructor
//    }
//
//
//    @Test
//    void testGetCatalogID() {
//        assertEquals("P1", product.getCatalogID());
//    }
//
//    @Test
//    void testGetName() {
//        assertEquals("Banana", product.getName());
//    }
//
//    @Test
//    void testSetName() {
//        product.setName("Apple");
//        assertEquals("Apple", product.getName());
//    }
//
//    @Test
//    void testGetAndSetPrice() {
//        assertEquals(10.0, product.getPrice());
//        product.setPrice(20.0);
//        assertEquals(20.0, product.getPrice());
//    }
//
//    @Test
//    void testGetAndSetExpirationDays() {
//        assertEquals(7, product.getExpirationDays());
//        product.setExpirationDays(14);
//        assertEquals(14, product.getExpirationDays());
//    }
//
//    @Test
//    void testAddDiscount() {
//        product.getDiscountDetails().put(10, 5.0);
//        assertEquals(5.0, product.getDiscountDetails().get(10));
//    }
//
//    @Test
//    void testRemoveDiscount() {
//        product.getDiscountDetails().put(10, 5.0);
//        product.getDiscountDetails().remove(10);
//        assertNull(product.getDiscountDetails().get(10));
//    }
//}
