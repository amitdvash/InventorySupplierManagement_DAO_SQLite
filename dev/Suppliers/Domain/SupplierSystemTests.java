//package dev.Suppliers.Domain;
//
//import org.junit.jupiter.api.Test;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SupplierSystemTests {
//
//    // Test for creating a new supplier
//    @Test
//    void testCreateNewSupplier() {
//        SupplierContact contact = new SupplierContact("John Doe", "123456789", "john@example.com");
//        Supplier supplier = new Supplier("123", "456", "789", "Credit", new ArrayList<>(), null, contact);
//        assertEquals("123", supplier.getSupplierID());
//        assertEquals("456", supplier.getCompanyID());
//        assertEquals("789", supplier.getBankAccount());
//        assertEquals("Credit", supplier.getPaymentMethod());
//        assertTrue(supplier.getProductList().isEmpty());
//        assertEquals(contact, supplier.getContact());
//    }
//
//    // Test for adding a product to supplier
//    @Test
//    void testAddProductToSupplier() {
//        SupplierContact contact = new SupplierContact("John Doe", "123456789", "john@example.com");
//        Supplier supplier = new Supplier("123", "456", "789", "Credit", new ArrayList<>(), null, contact);
//        Product product = new Product("001", "Catalog001", "Product A", new HashMap<>(), 10.0, 100, 0.5, null);
//        supplier.getProductList().add(product);
//        assertEquals(1, supplier.getProductList().size());
//        assertEquals("001", supplier.getProductList().get(0).getProductID());
//    }
//
//    // Test for setting supplier payment method
//    @Test
//    void testSetSupplierPaymentMethod() {
//        SupplierContact contact = new SupplierContact("John Doe", "123456789", "john@example.com");
//        Supplier supplier = new Supplier("123", "456", "789", "Credit", new ArrayList<>(), null, contact);
//        supplier.setPaymentMethod("Cash");
//        assertEquals("Cash", supplier.getPaymentMethod());
//    }
//
//    // Test for creating a new agreement
//    @Test
//    void testCreateAgreement() {
//        List<Product> products = new ArrayList<>();
//        products.add(new Product("001", "Catalog001", "Product A", new HashMap<>(), 10.0, 100, 0.5, null));
//        HashMap<String, HashMap<Integer, Double>> discountDetails = new HashMap<>();
//        HashMap<Integer, Double> discounts = new HashMap<>();
//        discounts.put(50, 0.1);
//        discountDetails.put("001", discounts);
//        Agreement agreement = new Agreement(1, products, new Date(), discountDetails, "Mon-Wed", false, "Credit");
//        assertEquals(1, agreement.getAgreementID());
//        assertEquals("001", agreement.getProductList().get(0).getProductID());
//    }
//
//    // Test for finding catalog number in discount document
//    @Test
//    void testFindCatalogNumberInAgreement() {
//        List<Product> products = new ArrayList<>();
//        products.add(new Product("001", "Catalog001", "Product A", new HashMap<>(), 10.0, 100, 0.5, null));
//        HashMap<String, HashMap<Integer, Double>> discountDetails = new HashMap<>();
//        HashMap<Integer, Double> discounts = new HashMap<>();
//        discounts.put(50, 0.1);
//        discountDetails.put("001", discounts);
//        Agreement agreement = new Agreement(1, products, new Date(), discountDetails, "Mon-Wed", false, "Credit");
//
//        assertTrue(agreement.getDiscountDetails().containsKey("001"));
//    }
//
//    // Test for adding a product to the agreement
//    @Test
//    void testAddProductToAgreement() {
//        List<Product> products = new ArrayList<>();
//        Product newProduct = new Product("002", "Catalog002", "Product B", new HashMap<>(), 20.0, 200, 1.0, null);
//        products.add(newProduct);
//        Agreement agreement = new Agreement(1, products, new Date(), new HashMap<>(), "Mon-Wed", false, "Credit");
//
//        assertEquals(1, agreement.getProductList().size());
//        assertEquals("002", agreement.getProductList().get(0).getProductID());
//    }
//
//    // Test for creating a new order
//    @Test
//    void testCreateOrder() {
//        List<Product> products = new ArrayList<>();
//        products.add(new Product("001", "Catalog001", "Product A", new HashMap<>(), 10.0, 100, 0.5, null));
//        Order order = new Order("ORD123", "SUP456", new Date(), products);
//
//        assertEquals("ORD123", order.getOrderID());
//        assertEquals("SUP456", order.getSupplierID());
//    }
//
//    // Test for adding a product to the order
//    @Test
//    void testAddProductToOrder() {
//        List<Product> products = new ArrayList<>();
//        Order order = new Order("ORD123", "SUP456", new Date(), products);
//        Product product = new Product("001", "Catalog001", "Product A", new HashMap<>(), 10.0, 100, 0.5, null);
//        order.getProductList().add(product);
//
//        assertEquals(1, order.getProductList().size());
//        assertEquals("001", order.getProductList().get(0).getProductID());
//    }
//
//    // Test for removing a product from the order
//    @Test
//    void testRemoveProductFromOrder() {
//        Product product = new Product("001", "Catalog001", "Product A", new HashMap<>(), 10.0, 100, 0.5, null);
//        List<Product> products = new ArrayList<>();
//        products.add(product);
//        Order order = new Order("ORD123", "SUP456", new Date(), products);
//
//        order.getProductList().remove(product);
//        assertEquals(0, order.getProductList().size());
//    }
//
//
//}
