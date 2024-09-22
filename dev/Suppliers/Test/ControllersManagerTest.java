
import dev.Suppliers.Domain.*;
import dev.Suppliers.Enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllersManagerTest {

    private ControllersManager controllersManager;
    private SupplierController supplierController;
    private ProductController productController;
    private AgreementController agreementController;
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        supplierController = new SupplierController();  // Using real instances
        productController = new ProductController();
        agreementController = new AgreementController();
        orderController = new OrderController();
        controllersManager = new ControllersManager(supplierController, productController, agreementController, orderController);
    }

    @Test
    public void testCreateSupplier() {
        // Act
        Supplier result = supplierController.createSupplier("S1", "12345", PaymentMethod.CreditCard, null, "Supplier One", "987654321", "supplier1@example.com");

        // Assert
        assertNotNull(result, "Supplier should be created successfully");
        assertEquals("S1", result.getSupplierID());
        assertEquals("Supplier One", result.getContact().getName());
    }

    @Test
    public void testAddProductToSupplier() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S1", "12345", PaymentMethod.CreditCard, null, "Supplier One", "987654321", "supplier1@example.com");
        HashMap<Integer, Double> productDiscounts = new HashMap<>();
        productDiscounts.put(10, 5.0);  // 5% discount for 10 or more units

        Product product = productController.createProduct("Product 1", productDiscounts, 20.0, 10, 1.0, null);
        Agreement agreement = agreementController.createAgreement(List.of(product), new HashMap<>(), List.of("Monday", "Wednesday"), true);

        // Act
        supplier.setSupplierAgreement(agreement);  // Add the product to the supplier agreement

        // Assert
        assertNotNull(supplier.getSupplierAgreement(), "Supplier should have an agreement");
        assertEquals(1, supplier.getSupplierAgreement().getProductList().size(), "Product list should have 1 product");
    }

    @Test
    public void testUpdateSupplierFields() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S2", "54321", PaymentMethod.Cash, null, "Supplier Two", "123456789", "supplier2@example.com");

        // Act
        supplier.getContact().setName("Updated Supplier Two");
        supplier.setPaymentMethod(PaymentMethod.BankTransfer);

        // Assert
        assertEquals("Updated Supplier Two", supplier.getContact().getName());
        assertEquals(PaymentMethod.BankTransfer, supplier.getPaymentMethod());
    }

    @Test
    public void testCreateAgreement() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S3", "67890", PaymentMethod.BankTransfer, null, "Supplier Three", "1357911", "supplier3@example.com");
        HashMap<Integer, Double> productDiscounts = new HashMap<>();
        productDiscounts.put(5, 10.0);  // 10% discount for 5 or more units

        Product product = productController.createProduct("Product 1", productDiscounts, 30.0, 50, 1.5, null);

        // Act
        Agreement agreement = agreementController.createAgreement(List.of(product), new HashMap<>(), List.of("Monday", "Friday"), true);
        supplier.setSupplierAgreement(agreement);

        // Assert
        assertNotNull(supplier.getSupplierAgreement(), "Agreement should be created successfully");
        assertEquals(1, supplier.getSupplierAgreement().getProductList().size(), "Agreement should have 1 product");
        assertEquals(product, supplier.getSupplierAgreement().getProductList().get(0));
    }

    @Test
    public void testDeleteSupplierCard() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S4", "78910", PaymentMethod.CreditCard, null, "Supplier Four", "24681012", "supplier4@example.com");

        // Act
        supplierController.deleteSupplier(supplier.getSupplierID());

        // Assert
        assertNull(supplierController.getSupplierById(supplier.getSupplierID()), "Supplier should be deleted successfully");
    }

    @Test
    public void testAddMultipleProductsToSupplier() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S5", "11111", PaymentMethod.Cash, null, "Supplier Five", "543210987", "supplier5@example.com");
        HashMap<Integer, Double> productDiscounts1 = new HashMap<>();
        productDiscounts1.put(5, 3.0);  // 3% discount for 5 or more units
        Product product1 = productController.createProduct("Product A", productDiscounts1, 10.0, 30, 2.0, null);

        HashMap<Integer, Double> productDiscounts2 = new HashMap<>();
        productDiscounts2.put(10, 5.0);  // 5% discount for 10 or more units
        Product product2 = productController.createProduct("Product B", productDiscounts2, 20.0, 50, 3.5, null);

        // Act
        Agreement agreement = agreementController.createAgreement(List.of(product1, product2), new HashMap<>(), List.of("Tuesday", "Thursday"), true);
        supplier.setSupplierAgreement(agreement);

        // Assert
        assertEquals(2, supplier.getSupplierAgreement().getProductList().size(), "Supplier agreement should have 2 products");
    }

    @Test
    public void testUpdateDiscountDetails() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S6", "22222", PaymentMethod.BankTransfer, null, "Supplier Six", "135791", "supplier6@example.com");
        HashMap<Integer, Double> productDiscounts = new HashMap<>();
        productDiscounts.put(5, 5.0);  // 5% discount for 5 or more units
        Product product = productController.createProduct("Product X", productDiscounts, 40.0, 60, 2.5, null);
        Agreement agreement = agreementController.createAgreement(List.of(product), new HashMap<>(), List.of("Wednesday", "Saturday"), true);
        supplier.setSupplierAgreement(agreement);

        // Act
        HashMap<Integer, Double> newDiscounts = new HashMap<>();
        newDiscounts.put(15, 10.0);  // 10% discount for 15 or more units
        product.setDiscountDetails(newDiscounts);  // Update the product's discount

        // Assert
        assertEquals(10.0, product.getDiscountDetails().get(15), "Discount should be updated to 10% for 15 units");
    }

    @Test
    public void testSupplierContactUpdate() {
        // Arrange
        Product product = productController.createProduct("Product Z", new HashMap<>(), 25.0, 100, 3.0, null);

        Agreement agreement = agreementController.createAgreement(List.of(product), new HashMap<>(), List.of("Monday"), true);

        Supplier supplier = supplierController.createSupplier("S9", "55555", PaymentMethod.Cash,agreement, "Old Name", "123456789", "oldemail@example.com");

        // Act
        supplier.getContact().setName("New Name");
        supplier.getContact().setPhoneNumber("987654321");
        supplier.getContact().setEmail("newemail@example.com");

        // Assert
        assertEquals("New Name", supplier.getContact().getName(), "Contact name should be updated");
        assertEquals("987654321", supplier.getContact().getPhoneNumber(), "Contact phone number should be updated");
        assertEquals("newemail@example.com", supplier.getContact().getEmail(), "Contact email should be updated");
    }


    @Test
    public void testUpdateMultipleSupplierFields() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S11", "33333", PaymentMethod.Cash, null, "Supplier Eleven", "654321987", "supplier11@example.com");

        // Act - Update multiple fields
        supplier.setCompanyID("99999");
        supplier.setBankAccount("987654321");
        supplier.setPaymentMethod(PaymentMethod.CreditCard);
        supplier.getContact().setName("Updated Supplier Eleven");
        supplier.getContact().setPhoneNumber("123456789");
        supplier.getContact().setEmail("updatedsupplier11@example.com");

        // Assert
        assertEquals("99999", supplier.getCompanyID(), "Company ID should be updated");
        assertEquals("987654321", supplier.getBankAccount(), "Bank account should be updated");
        assertEquals(PaymentMethod.CreditCard, supplier.getPaymentMethod(), "Payment method should be updated");
        assertEquals("Updated Supplier Eleven", supplier.getContact().getName(), "Supplier name should be updated");
        assertEquals("123456789", supplier.getContact().getPhoneNumber(), "Phone number should be updated");
        assertEquals("updatedsupplier11@example.com", supplier.getContact().getEmail(), "Email should be updated");
    }


    @Test
    public void testRemoveProductFromSupplierAgreement() {
        // Arrange
        Supplier supplier = supplierController.createSupplier("S12", "44444", PaymentMethod.CreditCard, null, "Supplier Twelve", "1122334455", "supplier12@example.com");

        HashMap<Integer, Double> productDiscounts = new HashMap<>();
        productDiscounts.put(10, 10.0);  // 10% discount for 10 or more units

        Product product1 = productController.createProduct("Product Y", productDiscounts, 50.0, 20, 5.0, null);
        Product product2 = productController.createProduct("Product Z", productDiscounts, 30.0, 15, 2.0, null);

        // Create an agreement with product1
        Agreement agreement = agreementController.createAgreement(new ArrayList<>(List.of(product1)), new HashMap<>(), List.of("Tuesday", "Thursday"), true);
        supplier.setSupplierAgreement(agreement);

        // Act - Remove product1 (which exists) and try to remove product2 (which does not exist)
        supplier.getSupplierAgreement().removeProduct(product1);  // Should succeed
        supplier.getSupplierAgreement().removeProduct(product2);  // Should fail

        // Assert
        assertEquals(0, supplier.getSupplierAgreement().getProductList().size(), "Product list should be empty after removing product1");
        assertFalse(supplier.getSupplierAgreement().getProductList().contains(product1), "Product Y should be removed from the agreement");
        assertFalse(supplier.getSupplierAgreement().getProductList().contains(product2), "Product Z should not be removed because it was not in the agreement");
    }



}
